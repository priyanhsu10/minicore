package minicore.mvc.formatters.input;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IInputFormatter;
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
                return bodyType.newInstance();
            }
            return JsonHelper.deserialize(data,bodyType);

        } catch (IOException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canSupport(String context) {
        return supportedMediaType().equals(context);
    }

}
