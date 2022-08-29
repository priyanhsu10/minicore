package minicore.contracts.results;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IFormatProvider;
import minicore.contracts.formaters.IOutputFormatter;

public class JsonResult implements IActionResult {

    public Object getValue() {
        return value;
    }

    private final Object value;

    public JsonResult(Object value) {


        this.value = value;
    }

    @Override
    public void executeResult(HttpContext context) {

       IFormatProvider formatProvider=HttpContext.services.resolve(IFormatProvider.class);
        IOutputFormatter formatter =formatProvider.getSupportedOutputFormatter(context.ActionContext.OutputMediaType);
        //provide json options
        formatter.WriteResponse(context);
    }

    @Override
    public int getHttpStatus() {
        return HttpStatus.OK;
    }
}
