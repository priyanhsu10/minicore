package minicore.contracts.modelbinding;

import minicore.contracts.HttpContext;

import java.util.Map;

public interface IModelValueCollector {
     Map<String, Object> getQueryParameters();
    Object getBodyData();
    Map<String, Object> getRouteData();
    Map<String, String> getHeaders();
    void  setHttpContext(HttpContext httpContext);
}
