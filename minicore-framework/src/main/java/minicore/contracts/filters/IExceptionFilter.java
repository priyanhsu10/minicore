package minicore.contracts.filters;

import minicore.contracts.HttpContext;

public interface IExceptionFilter {

    void onException(HttpContext context ,Exception e);
}
