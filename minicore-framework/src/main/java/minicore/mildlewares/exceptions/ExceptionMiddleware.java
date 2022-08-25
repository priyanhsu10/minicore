package minicore.mildlewares.exceptions;

import minicore.contracts.HttpContext;
import minicore.contracts.IAction;
import minicore.contracts.IActionDelegate;
import minicore.contracts.IMiddleware;
import minicore.json.JsonHelper;

import java.io.IOException;

public class ExceptionMiddleware implements IMiddleware {

    public ExceptionMiddleware(){

    }

    @Override
    public void next(IActionDelegate action, HttpContext actionContext) throws IOException {
        try {

            action.invoke(actionContext);
        } catch (Exception exception) {
            Error e = new Error();

            e.setMessage(exception.getLocalizedMessage());
            e.setErrorCode(500);
            ExceptionResult result = new ExceptionResult(e);
            String data = JsonHelper.serialize(result, ExceptionResult.class);
            actionContext.getResponse().getWriter().println(data);
            actionContext.getResponse().setStatus(500);


        }

    }
}
