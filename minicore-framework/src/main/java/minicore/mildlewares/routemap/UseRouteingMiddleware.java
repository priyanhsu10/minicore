package minicore.mildlewares.routemap;

import minicore.contracts.*;
import minicore.contracts.IMiddleware;
import minicore.host.WebHostBuilder;
import minicore.mildlewares.exceptions.MvcException;

import java.io.IOException;

public class UseRouteingMiddleware implements IMiddleware {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String ACCEPT = "Accept";
    private static final String APPLICATION_JSON = "application/json";
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
            throw new MvcException(e);
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
            // write not found result
            return;
        }
        context.setEndPointMetadata(e);
        createActionContext(context);
    }

    private void createActionContext(HttpContext context) {
        ActionContext actionContext = new ActionContext();
        // set content type
        actionContext.OutputMediaType = context.getRequest().getHeader(ACCEPT);
        actionContext.OutputMediaType = actionContext.OutputMediaType == null ? APPLICATION_JSON
                : actionContext.OutputMediaType;

        actionContext.InputMediaType = context.getRequest().getHeader(CONTENT_TYPE);
        actionContext.InputMediaType = actionContext.InputMediaType == null ? APPLICATION_JSON
                : actionContext.InputMediaType;
        context.ActionContext = actionContext;
    }
}
