package minicore.contracts.results;

import minicore.contracts.HttpContext;

public class ContentResult implements  IActionResult{
    private  Object value;
    public ContentResult(Object value) {
     this.value=value;
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
        return 0;
    }
}
