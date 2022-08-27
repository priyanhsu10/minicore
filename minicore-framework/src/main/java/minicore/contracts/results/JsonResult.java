package minicore.contracts.results;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IFormatProvider;

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

       IFormatProvider formatter=HttpContext.services.resolve(IFormatProvider.class);
       formatter.getSupportedOutputFormatter(context);

    }

    @Override
    public int getHttpStatus() {
        return HttpStatus.OK;
    }
}
