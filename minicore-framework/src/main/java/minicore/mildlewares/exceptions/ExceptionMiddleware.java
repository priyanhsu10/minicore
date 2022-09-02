package minicore.mildlewares.exceptions;

import minicore.contracts.HttpContext;
import minicore.contracts.IAction;
import minicore.contracts.IActionDelegate;
import minicore.contracts.IMiddleware;
import minicore.json.JsonHelper;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.stream.Collectors;

public class ExceptionMiddleware implements IMiddleware {
    private Logger logger;
    public ExceptionMiddleware(ILoggerFactory iLoggerFactory){
        logger= iLoggerFactory.getLogger("ExceptionMiddleware");
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
            logger.error(exception.getLocalizedMessage(),exception);
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
