package minicore.contracts.formaters;

import minicore.contracts.HttpContext;

public interface IOutputFormatter {
    String supportedMediaType();

    boolean canSupport(String mediaType);

    void WriteResponse(HttpContext context);
}
