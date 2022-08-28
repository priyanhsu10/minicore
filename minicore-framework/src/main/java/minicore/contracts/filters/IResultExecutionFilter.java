package minicore.contracts.filters;

import minicore.contracts.HttpContext;

public interface IResultExecutionFilter {
    void beforeResultExecute(HttpContext httpContext);
}
