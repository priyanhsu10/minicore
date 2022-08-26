package minicore.contracts;

import minicore.contracts.ioc.IServiceCollection;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class HttpContext {
    private final HashMap<String, Object> data;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private EndPoint endpoint;

    private Object actionResult;
    public  static IServiceCollection services;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    private String  route;

    public Object getActionResult() {
        return actionResult;
    }

    public void setActionResult(Object actionResult) {
        this.actionResult = actionResult;
    }

    public EndPoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(EndPoint endpoint) {
        this.endpoint = endpoint;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
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

    HttpContext() {
        data = new HashMap<>();
    }
}
