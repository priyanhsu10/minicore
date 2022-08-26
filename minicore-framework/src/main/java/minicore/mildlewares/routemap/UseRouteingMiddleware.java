package minicore.mildlewares.routemap;


import minicore.contracts.*;
import minicore.contracts.IMiddleware;
import minicore.host.WebHostBuilder;

import java.io.IOException;
import java.io.PrintWriter;

public class UseRouteingMiddleware implements IMiddleware {
    //inject dependency
    private IActionDelegate action;

    public UseRouteingMiddleware() {

    }

    @Override
    public void setNext(IActionDelegate action) {
        this.action = action;
    }

    @Override
    public void next(HttpContext httpContext) throws Exception {


        try {
            setRoutingData(httpContext);
            action.invoke(httpContext);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void setRoutingData(HttpContext actionContext) throws IOException {
        PrintWriter p = actionContext.getResponse().getWriter();
        String routPath = actionContext.getRequest().getRequestURI();
        if (routPath.startsWith("/")) {
            routPath = routPath.substring(1);
        }
        actionContext.setRoute(routPath);
        EndPoint e = WebHostBuilder.getEndPointManger().getEndPoint(routPath, actionContext.getRequest().getMethod());
        actionContext.setEndpoint(e);
    }
}
