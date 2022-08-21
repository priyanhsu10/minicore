package minicore.mildlewares.routemap;



import minicore.contracts.*;
import minicore.host.WebHostBuilder;

import java.io.IOException;
import java.io.PrintWriter;

public class UseRouteingMiddleware implements IMiddleware {
    private IAction _action;

    public UseRouteingMiddleware(IAction action) {


        _action = action;
    }

    @Override
    public void invoke(HttpContext actionContext) throws Exception {


        try {
            setRoutingData(actionContext);
            _action.next(actionContext);

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private void setRoutingData(HttpContext actionContext) throws IOException {
        PrintWriter p = actionContext.getResponse().getWriter();
        String routPath = actionContext.getRequest().getRequestURI();
        if(routPath.startsWith("/")){
            routPath=routPath.substring(1);
        }
        actionContext.setRoute(routPath);
        EndPoint e = WebHostBuilder.getEndPointManger().getEndPoint(routPath, actionContext.getRequest().getMethod());
        actionContext.setEndpoint(e);
    }
}
