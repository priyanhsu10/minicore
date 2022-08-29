package minicore.contracts.formaters;

import minicore.contracts.HttpContext;

public interface IInputFormatter {
    boolean canSupport(String context);
    String supportedMediaType();

    Object format(HttpContext context,Class bodyType);
}
