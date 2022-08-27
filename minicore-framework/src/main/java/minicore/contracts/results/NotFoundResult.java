package minicore.contracts.results;

import minicore.contracts.HttpContext;

public class NotFoundResult extends ObjectResult{
    public NotFoundResult(Object value) {
        super(value);
    }

    @Override
    public void executeResult(HttpContext context) {
        super.executeResult(context);
    }

    @Override
    public int getHttpStatus() {
        return HttpStatus.NotFound;
    }
}
