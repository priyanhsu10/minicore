package minicore.contracts.results;

import minicore.contracts.HttpContext;

public interface IActionResult {
    public Object getValue();
    void executeResult(HttpContext context);
    int getHttpStatus();
}
