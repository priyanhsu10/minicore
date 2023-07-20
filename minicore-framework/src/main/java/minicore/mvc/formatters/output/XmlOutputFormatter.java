package minicore.mvc.formatters.output;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IOutputFormatter;
import minicore.json.XMLHelper;
import minicore.mildlewares.exceptions.FormaterExeption;

import java.io.IOException;

public class XmlOutputFormatter implements IOutputFormatter {

    @Override
    public String supportedMediaType() {
        return "application/xml";
    }

    @Override
    public boolean canSupport(String mediaType) {
        return supportedMediaType().equals(mediaType);
    }

    @Override
    public void WriteResponse(HttpContext context) {
        try {
            String value = XMLHelper.serialize(context.ActionContext.ActionResult.getValue());
            context.getResponse().getWriter().write(value);
            context.getResponse().setStatus(context.ActionContext.ActionResult.getHttpStatus());
            context.getResponse().addHeader("Content-Type", supportedMediaType());

        } catch (IOException e) {
            throw new FormaterExeption(e);
        }
    }

}
