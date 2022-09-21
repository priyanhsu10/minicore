package minicore.mildlewares.endpointexecuting;


import minicore.contracts.HttpContext;
import minicore.contracts.IActionDelegate;
import minicore.contracts.IMiddleware;
import minicore.contracts.mvc.IMvcHandler;

public class EndPointExecutorMiddleware implements IMiddleware {

    private IActionDelegate action;

    @Override
    public void setNext(IActionDelegate action) {
        this.action = action;
    }

    @Override
    public void next(HttpContext httpContext) throws Exception {
        //per request
        IMvcHandler mvcHandler = HttpContext.services.resolve(IMvcHandler.class);
        //set result;
        mvcHandler.process(httpContext);

        mvcHandler.executeResult(httpContext);

        //pipeline end
        //if you want to execute extra logic after response write
        if (action != null) {
            action.invoke(httpContext);
        }
    }
}
