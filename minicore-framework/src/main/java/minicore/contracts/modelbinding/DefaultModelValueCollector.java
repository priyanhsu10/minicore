package minicore.contracts.modelbinding;

import minicore.contracts.EndPointMetadata;
import minicore.contracts.HttpContext;
import minicore.contracts.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultModelValueCollector implements IModelValueCollector {
    private Map<String, Object> queryParameters = new HashMap<>();
    private Object bodyData;
    private Map<String, Object> routeData = new HashMap<>();
    private Map<String, String[]> formData = new HashMap<>();
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
    public Map<String, String[]> getFormData() {
        return formData;
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

        String method = httpContext.getEndPointMetadata().HttpMethod;
        // if(method.equals("GET") || method.equals("DELETE") ){
        // no need to read the body form request
        this.queryParameters = queryStringBinder(httpContext.getRequest().getQueryString());
        this.headers = collectHeaderValues(httpContext.getRequest());

        if (httpContext.getEndPointMetadata().isPattern) {
            routeData = routeDataBinder(httpContext.getEndPointMetadata(),
                    httpContext.getRoute());
        }
        if(method.equals(HttpMethod.POST )||method.equals( HttpMethod.PUT)){
       //collect form data
         this.formData=httpContext.getRequest().getParameterMap();

        }


    }

    private Map<String, String> collectHeaderValues(HttpServletRequest request) {

        Map<String, String> headerMap = new HashMap<>();
        if (request.getHeaderNames() != null) {
            List<String> headers = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toList());
            headers.stream().forEach(x -> headerMap.put(x, request.getHeader(x)));
        }
        return headerMap;
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

    public Map<String, Object> routeDataBinder(EndPointMetadata endPointMetadata, String routePath) {

        Map<String, Object> routData = new HashMap<>();
        String[] segments = routePath.split("/");
        String[] tokens = endPointMetadata.UrlTokens;
        for (int i = 1; i < tokens.length; i++) {
            if (tokens[i].startsWith(":")) {
                String value = segments[i];
                String parameterName = tokens[i].substring(1);
                Class<?> parameterType = endPointMetadata.ParameterNameTypes.get(parameterName);
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

}
