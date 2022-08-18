package minicore.mildlewares.modelbinding;


import minicore.contracts.EndPoint;
import minicore.contracts.HttpContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelBindingResult {
    private final Map<String, Object> queryParameters = new HashMap<>();
    private String bodyData;
   private Map<String, Object> routeData = new HashMap<>();
    private final   Map<String, String> header = new HashMap<>();

    public Map<String, Object> getQueryParameters() {
        return queryParameters;
    }

    public String getBodyData() {
        return bodyData;
    }

    public Map<String, Object> getRouteData() {
        return routeData;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public ModelBindingResult(HttpContext actionContext) {

        processBinding(actionContext);
    }

    private void processBinding(HttpContext actionContext) {


        String method = actionContext.getEndpoint().HttpMethod;
//        if(method.equals("GET") || method.equals("DELETE") ){
        //no need to read the body form request
        String query = actionContext.getRequest().getQueryString();
        if (query != null && !query.isEmpty()) {

            Arrays.stream(query.split("&")).forEach(x -> {
                if (x.contains("=")) {
                    String[] keyValue = x.split("=");
                    queryParameters.put(keyValue[0], keyValue[1]);
                }

            });
        }
        if (actionContext.getEndpoint().isPattern) {
            routeData = getRouteData(actionContext.getEndpoint(),
                    actionContext.getRoute());
        }
        if (method.equals("PUT") || method.equals("POST")) {
            try {
                bodyData = actionContext.getRequest()
                        .getReader()
                        .lines()
                        .collect(Collectors.joining());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    private Map<String, Object> getRouteData(EndPoint endPoint, String routePath) {

        Map<String, Object> routData = new HashMap<>();
        String[] segments = routePath.split("/");
        String[] tokens = endPoint.UrlTokens;
        for (int i = 1; i < tokens.length; i++) {
            if (tokens[i].startsWith(":")) {
                String value = segments[i];
                String parameterName = tokens[i].substring(1);
                Class<?> parameterType = endPoint.ParameterNameTypes.get(parameterName);
                routData.put(parameterName,getObject( value,parameterType));
            }
        }
        return routData;

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
