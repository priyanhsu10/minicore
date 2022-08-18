package minicore.endpoints;

import minicore.contracts.ControllerBase;
import minicore.contracts.EndPoint;
import minicore.endpoints.annotations.*;
import minicore.host.IStartup;
import minicore.ioc.IServiceCollection;
import minicore.ioc.container.Scope;

import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EndPointManger {
    public static List<EndPoint> endPoints = new ArrayList<>();
    private final Set<Class<? extends ControllerBase>> controllerClasses;
    private IServiceCollection serviceCollection;

    public EndPointManger(Class<? extends IStartup> aClass, IServiceCollection serviceCollection) {
        this.serviceCollection = serviceCollection;
        System.out.println(aClass.getPackage().getName());
        Reflections reflections = new Reflections(aClass.getPackage().getName());

        this.controllerClasses = reflections.getSubTypesOf(ControllerBase.class);
        registerControllers();
        process();

    }

    private void registerControllers() {
        for (Class<? extends ControllerBase> c : controllerClasses) {
            serviceCollection.register(c, Scope.RequestScope);
        }

    }

    private void process() {
        for (Class<? extends ControllerBase> controller : controllerClasses) {

            //get base path from route annotation

            Route r = controller.getAnnotation(Route.class);
            String BaseControllerPath = r == null ? "" : r.path();
            prepareEndPoint(controller, BaseControllerPath);


        }

    }

    private void prepareEndPoint(Class<? extends ControllerBase> controller, String BaseControllerPath) {
        Method[] declaredMethods = controller.getDeclaredMethods();
        for (int j = 0, declaredMethodsLength = declaredMethods.length; j < declaredMethodsLength; j++) {
            Method m = declaredMethods[j];

            if (!Modifier.isPublic(m.getModifiers())) {
                continue;
            }

            EndPoint endPoint = new EndPoint(m, controller);
            if (m.isAnnotationPresent(Get.class)) {
                endPoint.HttpMethod = "GET";
                endPoint.DisplayName = m.getAnnotation(Get.class).path();


            } else if (m.isAnnotationPresent(Post.class)) {
                endPoint.HttpMethod = "POST";
                endPoint.DisplayName = m.getAnnotation(Post.class).path();


            } else if (m.isAnnotationPresent(Put.class)) {
                endPoint.HttpMethod = "PUT";
                endPoint.DisplayName = m.getAnnotation(Put.class).path();

            } else if (m.isAnnotationPresent(Delete.class)) {
                endPoint.HttpMethod = "DELETE";
                endPoint.DisplayName = m.getAnnotation(Delete.class).path();


            }

            endPoint.DisplayName = BaseControllerPath + endPoint.DisplayName;


            if (endPoint.DisplayName == null ) {
                continue;
            }
            if(endPoint.DisplayName.startsWith("/")){
                endPoint.DisplayName= endPoint.DisplayName.substring(1);
            }
            endPoint.UrlTokens = endPoint.DisplayName.split("/");
            if (endPoint.DisplayName.contains(":")) {
                //contain the pattern
                //example account/:name/test/:id
                endPoint.isPattern = true;
                Arrays.stream(endPoint.UrlTokens).filter(x -> x.contains(":")).forEach(x -> {
                    String paramName = x.substring(1);
                    Parameter[] parameters =m.getParameters();

                    Class<?>[] types= m.getParameterTypes();
                    for(int i=0;i<m.getParameterCount();i++){
                      if(   parameters[i].getName().equalsIgnoreCase(paramName)){
                          endPoint.ParameterNameTypes.put(paramName, types[i]);
                          break;
                      }
                    }


                });


            }


            endPoints.add(endPoint);

        }
    }

    public EndPoint getEndPoint(String routPath, String method) {
        String[] segments = routPath.split("/");
        List<EndPoint> candidates = endPoints.stream()
                .filter(x -> x.DisplayName.startsWith(segments[0]))
                .filter(y -> y.HttpMethod.equals(method))
                .filter(e -> e.UrlTokens.length == segments.length)
                .collect(Collectors.toList());
        if (candidates.isEmpty()) {
            throw new RuntimeException("no url match");

        }

      EndPoint selected=  candidates.stream().filter(x->isEndpointMatch(x,routPath)).findFirst().orElseThrow(()->
                new RuntimeException("No route path Match"));


        return  selected;



    }
    //this logic will be  use in route data calculation
    private boolean isEndpointMatch(EndPoint endPoint, String routPath) {
        String[] segments = routPath.split("/");
        String[] tokens = endPoint.DisplayName.split("/");
        if (!endPoint.isPattern && endPoint.DisplayName.equals(routPath)) {
            //check and match for parameters

            return true;

        } else {
            List<Object> routeParameters = new ArrayList<>();

            for (int i = 1; i < tokens.length; i++) {
                if (tokens[i].startsWith(":")) {
                    String value = segments[i];
                    String parameterName = tokens[i].substring(1);
                    Class<?> parameterType = endPoint.ParameterNameTypes.get(parameterName);
                    routeParameters.add(getObject(value, parameterType));
                } else {
                    if (!tokens[i].equals(segments[i])) {
                        //not match
                        return false;

                    }
                }

            }
            return true;
        }
    }

    private Object getObject(String value, Class<?> parameterType) {
        if(parameterType.isAssignableFrom(String.class)){
            return value;
        }
        if(parameterType.isAssignableFrom(Integer.class) || parameterType.isAssignableFrom(int.class)){
            return Integer.parseInt(value);
        }
        if(parameterType.isAssignableFrom(Double.class)|| parameterType.isAssignableFrom(double.class)){
            return Double.parseDouble(value);
        }
        if(parameterType.isAssignableFrom(Boolean.class)|| parameterType.isAssignableFrom(boolean.class)){
            return Boolean.parseBoolean(value);
        }
        return parameterType.cast(value);
    }
}


