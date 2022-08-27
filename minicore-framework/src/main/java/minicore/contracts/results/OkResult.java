package minicore.contracts.results;

import minicore.contracts.HttpContext;

public class OkResult extends  ObjectResult{
    public OkResult(Object value) {
        super(value);
    }

    @Override
    public void executeResult(HttpContext context) {
        super.executeResult(context);
    }

    @Override
    public int getHttpStatus() {
        return HttpStatus.OK;
    }
}
