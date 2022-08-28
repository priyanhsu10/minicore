package minicore.mvc;

import minicore.contracts.HttpContext;
import minicore.contracts.filters.IResultExecutionFilter;

public class DefaultResultFilter implements IResultExecutionFilter {
    @Override
    public void beforeResultExecute(HttpContext httpContext) {
        //add custom headers
    }
}
