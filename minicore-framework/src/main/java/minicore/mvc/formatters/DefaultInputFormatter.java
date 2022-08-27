package minicore.mvc.formatters;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IInputFormatter;

import java.io.IOException;
import java.util.stream.Collectors;

public class DefaultInputFormatter implements IInputFormatter {
    @Override
    public boolean canSupport(HttpContext context) {
        return false;
    }

    @Override
    public String supportedMediaType() {
        return "*";
    }

    @Override
    public Object format(HttpContext context) {
        try {
            return context.getRequest()
                    .getReader()
                    .lines()
                    .collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }
}
