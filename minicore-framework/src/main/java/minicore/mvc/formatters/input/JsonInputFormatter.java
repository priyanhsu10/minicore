package minicore.mvc.formatters.input;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IInputFormatter;
import minicore.contracts.formaters.IOutputFormatter;
import minicore.json.JsonHelper;

import java.io.IOException;
import java.util.stream.Collectors;

public class JsonInputFormatter implements IInputFormatter {

    @Override
    public String supportedMediaType() {
        return "application/json";
    }

    @Override
    public Object format(HttpContext context,Class bodyType) {
        try {
            String data= context.getRequest()
                    .getReader()
                    .lines()
                    .collect(Collectors.joining());
            if(data.isEmpty() || data==null){
                return data;
            }
            return JsonHelper.deserialize(data,bodyType);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canSupport(HttpContext context) {
        return context.getEndpoint().InputMediaType.equals("application/json");
    }

    public void WriteResponse(HttpContext context)  {
        try {
            String value= JsonHelper.serialize(context.getActionResult().getValue());
            context.getResponse().getWriter().write(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
