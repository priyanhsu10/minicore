package minicore.mvc.formatters.output;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IOutputFormatter;
import minicore.json.XMLHelper;

import java.io.IOException;

public class XmlOutputFormatter implements IOutputFormatter {

    @Override
    public String supportedMediaType() {
        return "application/xml";
    }

    @Override
    public boolean canSupport(HttpContext context) {
        return context.ActionContext.OutputMediaType.equals("application/xml");
    }

    @Override
    public void WriteResponse(HttpContext context)  {
        try {
            String value= XMLHelper.serialize(context.ActionContext.ActionResult.getValue());
            context.getResponse().getWriter().write(value);
            context.getResponse().setStatus(context.ActionContext.ActionResult.getHttpStatus());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
