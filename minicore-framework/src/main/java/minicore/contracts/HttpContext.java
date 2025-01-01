package minicore.contracts;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.results.IActionResult;


import java.util.HashMap;

public class HttpContext {
    //custom data can keep
    private final HashMap<String, Object> data;
    public ActionContext ActionContext;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private EndPointMetadata endPointMetadata;
    public  static IServiceCollection services;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    private String  route;


    public EndPointMetadata getEndPointMetadata() {
        return endPointMetadata;
    }

    public void setEndPointMetadata(EndPointMetadata endPointMetadata) {
        this.endPointMetadata = endPointMetadata;
    }


    public HttpServletResponse getResponse() {
        return response;
    }


    public HashMap<String, Object> getData() {
        return data;
    }

    public HttpServletRequest getRequest() {
        return request;
    }


    public HttpContext(ServletRequest servletRequest, ServletResponse servletResponse) {
        this.request = (HttpServletRequest)servletRequest;
        this.response =(HttpServletResponse) servletResponse;
        data = new HashMap<>();
    }

}
