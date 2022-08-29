package minicore.contracts.results;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IFormatProvider;
import minicore.contracts.formaters.IOutputFormatter;

public class ObjectResult extends ActionResult {
    public ObjectResult(Object value, int httpStatus) {
        super(value, httpStatus);
    }

    public ObjectResult(Object value) {
        super(value);
    }

    @Override
    public void executeResult(HttpContext context) {
        IFormatProvider provider = HttpContext.services.resolve(IFormatProvider.class);
        IOutputFormatter formatter = provider.getSupportedOutputFormatter(context.ActionContext.OutputMediaType);
        formatter.WriteResponse(context);

    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

}
