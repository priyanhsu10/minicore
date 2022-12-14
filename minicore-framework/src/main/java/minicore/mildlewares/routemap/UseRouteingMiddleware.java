package minicore.mildlewares.routemap;

import minicore.contracts.*;
import minicore.contracts.IMiddleware;
import minicore.contracts.modelbinding.DefaultModelValueCollector;
import minicore.host.WebHostBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class UseRouteingMiddleware implements IMiddleware {
    // inject dependency
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

    private void setRoutingData(HttpContext context) throws IOException {
        String routPath = context.getRequest().getRequestURI();
        if (routPath.startsWith("/")) {
            routPath = routPath.substring(1);
        }
        context.setRoute(routPath);
        EndPointMetadata e = WebHostBuilder.getEndPointManger().getEndPoint(routPath, context.getRequest().getMethod());
        if (e == null) {
            //write not found result
            return;
        }
        context.setEndPointMetadata(e);
        createActionContext(context);
    }

    private void createActionContext(HttpContext context) {
        ActionContext actionContext = new ActionContext();
        // set content type
        actionContext.OutputMediaType = context.getRequest().getHeader("Accept");
        actionContext.OutputMediaType = actionContext.OutputMediaType == null ? "application/json" : actionContext.OutputMediaType;

        actionContext.InputMediaType = context.getRequest().getHeader("Content-Type");
        actionContext.InputMediaType = actionContext.InputMediaType == null ? "application/json" : actionContext.InputMediaType;
        context.ActionContext = actionContext;
    }
}
