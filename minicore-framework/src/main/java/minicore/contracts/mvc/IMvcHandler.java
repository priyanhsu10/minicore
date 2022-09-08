package minicore.contracts.mvc;

import minicore.contracts.HttpContext;

public interface IMvcHandler {
    void process(HttpContext httpContext) throws Exception;
}
