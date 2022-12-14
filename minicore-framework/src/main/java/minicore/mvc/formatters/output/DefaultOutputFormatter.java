package minicore.mvc.formatters.output;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IOutputFormatter;
import minicore.json.JsonHelper;

import java.io.IOException;
import java.util.HashSet;

public class DefaultOutputFormatter implements IOutputFormatter {
    private HashSet<String> supportedMediatTypes= new HashSet<>();

    public DefaultOutputFormatter() {
        supportedMediatTypes.add("text/plain");
        supportedMediatTypes.add("text/html");
        supportedMediatTypes.add("text/javascript");
    }

    @Override
    public boolean canSupport(String mediaType) {
        return supportedMediatTypes.contains(mediaType);
    }

    @Override
    public String supportedMediaType() {
        return "text/plain";
    }

    @Override
    public void WriteResponse(HttpContext context) {
        try {
            String value = JsonHelper.serialize(context.ActionContext.ActionResult.getValue());
            context.getResponse().getWriter().write(value);
            context.getResponse().setStatus(context.ActionContext.ActionResult.getHttpStatus());
            context.getResponse().addHeader("Content-Type","application/json");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


