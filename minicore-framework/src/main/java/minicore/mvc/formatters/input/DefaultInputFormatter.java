package minicore.mvc.formatters.input;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IInputFormatter;
import minicore.mildlewares.exceptions.FormaterExeption;

import java.io.IOException;
import java.util.stream.Collectors;

public class DefaultInputFormatter implements IInputFormatter {
    @Override
    public boolean canSupport(String context) {
        return false;
    }

    @Override
    public String supportedMediaType() {
        return "*";
    }

    @Override
    public Object format(HttpContext context, Class bodyType) {
        try {
            return context.getRequest()
                    .getReader()
                    .lines()
                    .collect(Collectors.joining());
        } catch (IOException e) {
            throw new FormaterExeption(e);
        }
    }
}
