package minicore.mvc.filters;

import minicore.contracts.HttpContext;
import minicore.contracts.filters.IExceptionFilter;
import minicore.contracts.results.HttpStatus;
import minicore.contracts.results.ObjectResult;

public class MvcExceptionFilter implements IExceptionFilter {
    @Override
    public boolean support(Class<? extends RuntimeException> exceptions) {
        return exceptions.equals(RuntimeException.class);
    }

    @Override
    public <T extends RuntimeException> void onException(HttpContext context, T e) {

        context.ActionContext.ActionResult= new ObjectResult(new MvcException(e.getLocalizedMessage()), HttpStatus.InternalServerError);
    }

}
