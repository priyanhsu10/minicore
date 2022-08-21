package minicore.mildlewares.exceptions;

import minicore.contracts.HttpContext;
import minicore.contracts.IAction;
import minicore.contracts.IMiddleware;
import minicore.json.JsonHelper;

import java.io.IOException;

public class ExceptionMiddleware implements IMiddleware {
    private IAction _action;

    public ExceptionMiddleware(IAction action){

        _action = action;
    }

    @Override
    public void invoke(HttpContext actionContext) throws IOException {
        try {

            _action.next(actionContext);
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
