package minicore.mvc.formatters;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IOutputFormatter;
import minicore.json.JsonHelper;

import java.io.IOException;

public class JsonOutputFormatter implements IOutputFormatter {

    @Override
    public boolean canSupport(HttpContext context) {
        return context.getEndpoint().OutputMediaType.equals("application/json");
    }

    @Override
    public void WriteResponse(HttpContext context)  {
        try {
            String value= JsonHelper.serialize(context.getActionResult(),context.getActionResult().getClass());
            context.getResponse().getWriter().write(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
