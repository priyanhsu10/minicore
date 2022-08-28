package minicore.contracts.formaters;

import minicore.contracts.HttpContext;
import minicore.contracts.results.IActionResult;

import java.io.IOException;

public interface IOutputFormatter {
    String supportedMediaType();
    boolean canSupport(HttpContext context);

    void WriteResponse(HttpContext context) ;
}
