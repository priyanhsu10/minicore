package minicore.contracts.modelbinding;


import minicore.contracts.EndPoint;
import minicore.contracts.HttpContext;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultModelValueCollector implements IModelValueCollector {
    private Map<String, Object> queryParameters = new HashMap<>();
    private Object bodyData;
    private String requestedMimeType;
    private String inputBodyContentType;
    private Map<String, Object> routeData = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private HttpContext httpContext;

    public Map<String, Object> getQueryParameters() {
        return queryParameters;
    }

    public Object getBodyData() {
        return bodyData;
    }

    public Map<String, Object> getRouteData() {
        return routeData;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void setHttpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
    }

    public DefaultModelValueCollector() {

    }

    public DefaultModelValueCollector(HttpContext context) {
        this.httpContext = context;
        collectDataFromRequest();
    }

    private void collectDataFromRequest() {


        String method = httpContext.getEndpoint().HttpMethod;
//        if(method.equals("GET") || method.equals("DELETE") ){
        //no need to read the body form request
        this.queryParameters = queryStringBinder(httpContext.getRequest().getQueryString());
        this.headers = collectHeaderValues(httpContext.getRequest());

        if (httpContext.getEndpoint().isPattern) {
            routeData = routeDataBinder(httpContext.getEndpoint(),
                    httpContext.getRoute());
        }
        if (method.equals("PUT") || method.equals("POST")) {
            try {
                bodyData = httpContext.getRequest()
                        .getReader()
                        .lines()
                        .collect(Collectors.joining());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        this.requestedMimeType = this.headers.getOrDefault("accept", "application/json");
        this.inputBodyContentType = this.headers.getOrDefault("content-type", "application/json");
    }

    private Map<String, String> collectHeaderValues(HttpServletRequest request) {

        Map<String, String> header = new HashMap<>();
        while (request.getHeaderNames().hasMoreElements()) {
            String key = request.getHeaderNames().nextElement();
            header.put(key, request.getHeader(key));
        }
        return header;
    }

    public Map<String, Object> queryStringBinder(String query) {
        Map<String, Object> queryMap = new HashMap<>();
        if (query != null && !query.isEmpty()) {

            Arrays.stream(query.split("&")).forEach(x -> {
                if (x.contains("=")) {
                    String[] keyValue = x.split("=");
                    queryMap.put(keyValue[0], keyValue[1]);
                }

            });
        }
        return queryMap;
    }

    public Map<String, Object> routeDataBinder(EndPoint endPoint, String routePath) {

        Map<String, Object> routData = new HashMap<>();
        String[] segments = routePath.split("/");
        String[] tokens = endPoint.UrlTokens;
        for (int i = 1; i < tokens.length; i++) {
            if (tokens[i].startsWith(":")) {
                String value = segments[i];
                String parameterName = tokens[i].substring(1);
                Class<?> parameterType = endPoint.ParameterNameTypes.get(parameterName);
                routData.put(parameterName, getObject(value, parameterType));
            }
        }
        return routData;

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

    public String getRequestedMimeType() {
        return requestedMimeType;
    }

    public String getInputBodyContentType() {
        return inputBodyContentType;
    }
}
