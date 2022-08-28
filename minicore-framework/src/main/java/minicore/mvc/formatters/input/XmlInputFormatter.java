package minicore.mvc.formatters.input;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IInputFormatter;
import minicore.contracts.formaters.IOutputFormatter;
import minicore.json.XMLHelper;

import java.io.IOException;
import java.util.stream.Collectors;

public class XmlInputFormatter implements IInputFormatter {



    @Override
    public String supportedMediaType() {
        return "application/xml";
    }


    @Override
    public Object format(HttpContext context,Class bodyType) {
        try {
            String body= context.getRequest()
                    .getReader()
                    .lines()
                    .collect(Collectors.joining());
          return   XMLHelper.deserialize(body,bodyType);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canSupport(HttpContext context) {
        return context.getEndpoint().OutputMediaType.equals("application/xml");
    }

    public void WriteResponse(HttpContext context)  {
        try {
            String value= XMLHelper.serialize(context.getActionResult().getValue());
            context.getResponse().getWriter().write(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
