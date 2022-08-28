package minicore.contracts;

import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.results.IActionResult;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class HttpContext {
    //custom data can keep
    private final HashMap<String, Object> data;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private EndPointMetadata endpoint;

    private IActionResult actionResult;
    public  static IServiceCollection services;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    private String  route;

    public IActionResult getActionResult() {
        return actionResult;
    }

    public void setActionResult(  IActionResult   actionResult) {
        this.actionResult = actionResult;
    }

    public EndPointMetadata getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(EndPointMetadata endpoint) {
        this.endpoint = endpoint;
    }
   //for test perpose
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
    //for test perpose
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
