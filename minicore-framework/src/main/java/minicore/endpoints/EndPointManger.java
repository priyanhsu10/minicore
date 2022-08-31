package minicore.endpoints;

import minicore.contracts.ControllerBase;
import minicore.contracts.EndPointMetadata;
import minicore.contracts.HttpMethod;
import minicore.contracts.annotations.http.*;
import minicore.contracts.host.IStartup;
import minicore.contracts.ioc.IServiceCollection;
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
    public static List<EndPointMetadata> endPointMetadata = new ArrayList<>();
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

            EndPointMetadata endPointMetadata = new EndPointMetadata(m, controller);
            setHttpMethod(m, endPointMetadata);

            endPointMetadata.DisplayName = BaseControllerPath + endPointMetadata.DisplayName;


            if (endPointMetadata.DisplayName == null) {
                continue;
            }
            if (endPointMetadata.DisplayName.startsWith("/")) {
                endPointMetadata.DisplayName = endPointMetadata.DisplayName.substring(1);
            }
            endPointMetadata.UrlTokens = endPointMetadata.DisplayName.split("/");

            patternMatch(m, endPointMetadata);


            EndPointManger.endPointMetadata.add(endPointMetadata);


        }
    }

    private void setHttpMethod(Method m, EndPointMetadata endPointMetadata) {
        if (m.isAnnotationPresent(Get.class)) {
            endPointMetadata.HttpMethod = HttpMethod.GET;
            endPointMetadata.DisplayName = m.getAnnotation(Get.class).path();


        } else if (m.isAnnotationPresent(Post.class)) {
            endPointMetadata.HttpMethod = HttpMethod.POST;
            endPointMetadata.DisplayName = m.getAnnotation(Post.class).path();


        } else if (m.isAnnotationPresent(Put.class)) {
            endPointMetadata.HttpMethod = HttpMethod.PUT;
            endPointMetadata.DisplayName = m.getAnnotation(Put.class).path();

        } else if (m.isAnnotationPresent(Delete.class)) {
            endPointMetadata.HttpMethod = HttpMethod.DELETE;
            endPointMetadata.DisplayName = m.getAnnotation(Delete.class).path();


        }
    }

    private void patternMatch(Method m, EndPointMetadata endPointMetadata) {
        if (!endPointMetadata.DisplayName.contains(":")) {
            return;
        }
        //contain the pattern
        //example account/:name/test/:id
        endPointMetadata.isPattern = true;
        Arrays.stream(endPointMetadata.UrlTokens).filter(x -> x.contains(":")).forEach(x -> {
            String paramName = x.substring(1);
            Parameter[] parameters = m.getParameters();

            Class<?>[] types = m.getParameterTypes();
            for (int i = 0; i < m.getParameterCount(); i++) {
                if (parameters[i].getName().equalsIgnoreCase(paramName)) {
                    endPointMetadata.ParameterNameTypes.put(paramName, types[i]);
                    break;
                }
            }


        });

    }

    public EndPointMetadata getEndPoint(String routPath, String method) {
        String[] segments = routPath.split("/");
        if (segments[0].length() == 0) {

            //base url  localhot:8090/
            throw new RuntimeException("no url match");
        }
        List<EndPointMetadata> candidates = endPointMetadata.stream()
                .filter(x -> x.DisplayName.startsWith(segments[0]))
                .filter(y -> y.HttpMethod.equals(method))
                .filter(e -> e.UrlTokens.length == segments.length)
                .collect(Collectors.toList());
        if (candidates.isEmpty()) {
            throw new RuntimeException("no url match");

        }

        EndPointMetadata selected = candidates.stream().filter(x -> isEndpointMatch(x, routPath)).findFirst().orElseThrow(() ->
                new RuntimeException("No route path Match"));


        return selected;


    }

    //this logic will be  use in route data calculation
    private boolean isEndpointMatch(EndPointMetadata endPointMetadata, String routPath) {
        String[] segments = routPath.split("/");
        String[] tokens = endPointMetadata.DisplayName.split("/");
        if (!endPointMetadata.isPattern && endPointMetadata.DisplayName.equals(routPath)) {
            //check and match for parameters

            return true;

        } else {
            List<Object> routeParameters = new ArrayList<>();

            for (int i = 1; i < tokens.length; i++) {
                if (tokens[i].startsWith(":")) {
                    String value = segments[i];
                    String parameterName = tokens[i].substring(1);
                    Class<?> parameterType = endPointMetadata.ParameterNameTypes.get(parameterName);
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
        if (parameterType.isAssignableFrom(String.class)) {
            return value;
        }
        if (parameterType.isAssignableFrom(Integer.class) || parameterType.isAssignableFrom(int.class)) {
            return Integer.parseInt(value);
        }
        if (parameterType.isAssignableFrom(Double.class) || parameterType.isAssignableFrom(double.class)) {
            return Double.parseDouble(value);
        }
        if (parameterType.isAssignableFrom(Boolean.class) || parameterType.isAssignableFrom(boolean.class)) {
            return Boolean.parseBoolean(value);
        }
        return parameterType.cast(value);
    }
}


