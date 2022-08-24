package minicore.contracts.filters;

import minicore.contracts.HttpContext;

public interface IActionFilter {
//todo:action specific context should pass .
    void beforeExecute(HttpContext httpContext);

    void afterExecute(HttpContext httpContext);
}

