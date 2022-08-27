package minicore.contracts.results;

import minicore.contracts.HttpContext;

public class BadRequestResult extends ObjectResult{
    public BadRequestResult(Object value) {
        super(value);
    }

    @Override
    public void executeResult(HttpContext context) {
        super.executeResult(context);
    }

    @Override
    public int getHttpStatus() {
        return super.getHttpStatus();
    }
}
