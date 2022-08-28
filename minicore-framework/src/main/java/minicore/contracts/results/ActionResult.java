package minicore.contracts.results;

import minicore.contracts.HttpContext;

public class ActionResult implements IActionResult {
    protected final Object value;
    protected int httpStatus = HttpStatus.OK;

    public ActionResult(Object value) {
        this.value = value;

    }

    public ActionResult(Object value, int httpStatus) {
        this.value = value;
        this.httpStatus = httpStatus;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void executeResult(HttpContext context) {

    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }
}
