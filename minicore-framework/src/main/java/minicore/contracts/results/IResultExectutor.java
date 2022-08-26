package minicore.contracts.results;

import minicore.contracts.HttpContext;

public interface IResultExectutor {
    void executeResult(HttpContext context , IActionResult result);
}
