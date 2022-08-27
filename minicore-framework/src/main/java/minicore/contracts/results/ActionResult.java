package minicore.contracts.results;

import minicore.contracts.HttpContext;

public class ActionResult implements IActionResult{
    protected final Object value;

    public ActionResult(Object  value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void executeResult(HttpContext context) {

    }

    @Override
    public int getHttpStatus() {
        return HttpStatus.OK;
    }
}
