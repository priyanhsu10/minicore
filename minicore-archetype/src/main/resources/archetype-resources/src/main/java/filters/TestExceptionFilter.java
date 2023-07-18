#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.filters;

import minicore.contracts.HttpContext;
import minicore.contracts.filters.IExceptionFilter;
import minicore.contracts.results.BadRequestResult;
import ${package}.exceptions.TestCustomException;

public class TestExceptionFilter implements IExceptionFilter {
    @Override
    public boolean support(Class<? extends RuntimeException> excetpions) {
        //from list of exception filter support method decide for handling exception
        return excetpions.equals(TestCustomException.class);
    }

    @Override
    public <T extends RuntimeException> void onException(HttpContext context, T e) {
        //handle your custom exception
        context.ActionContext.ActionResult= new BadRequestResult(e.getLocalizedMessage());
    }
}
