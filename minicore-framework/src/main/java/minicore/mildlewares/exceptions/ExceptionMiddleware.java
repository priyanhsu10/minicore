package minicore.mildlewares.exceptions;

import minicore.contracts.HttpContext;
import minicore.contracts.IAction;
import minicore.contracts.IActionDelegate;
import minicore.contracts.IMiddleware;
import minicore.json.JsonHelper;

import java.io.IOException;
import java.util.stream.Collectors;

public class ExceptionMiddleware implements IMiddleware {
    public ExceptionMiddleware(){

    }
    private  IActionDelegate action;

    @Override
    public void setNext(IActionDelegate action) {
        this.action=action;
    }

    @Override
    public void next( HttpContext httpContext) throws IOException {
        try {
            action.invoke(httpContext);
        } catch (Exception exception) {
            Error e = new Error();

            e.setMessage(exception.getLocalizedMessage());
            e.setErrorCode(500);
            ExceptionResult result = new ExceptionResult(e);
            String data = JsonHelper.serialize(result);
            httpContext.getResponse().getWriter().println(data);
            httpContext.getResponse().setStatus(500);


        }

    }
}
