package minicore.mvc.filters;

import minicore.contracts.HttpContext;
import minicore.contracts.filters.IExceptionFilter;
import minicore.contracts.results.HttpStatus;
import minicore.contracts.results.ObjectResult;

public class MvcExcetionFilter implements IExceptionFilter {
    @Override
    public boolean support(Class<? extends RuntimeException> excetpions) {
        return excetpions.equals(RuntimeException.class);
    }

    @Override
    public <T extends RuntimeException> void onException(HttpContext context, T e) {

        context.getEndpoint().actionResult= new ObjectResult(new MvcException(e), HttpStatus.InternalServerError);
    }

}
