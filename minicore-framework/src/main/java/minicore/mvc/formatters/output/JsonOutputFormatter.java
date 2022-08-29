package minicore.mvc.formatters.output;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IOutputFormatter;
import minicore.json.JsonHelper;

import java.io.IOException;

public class JsonOutputFormatter implements IOutputFormatter {

    @Override
    public String supportedMediaType() {
        return "application/json";
    }

    @Override
    public boolean canSupport(String mediaType) {
        return supportedMediaType().equals(mediaType);
    }

    @Override
    public void WriteResponse(HttpContext context)  {
        try {
            String value= JsonHelper.serialize(context.ActionContext.ActionResult.getValue());
            context.getResponse().getWriter().write(value);
            context.getResponse().setStatus(context.ActionContext.ActionResult.getHttpStatus());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
