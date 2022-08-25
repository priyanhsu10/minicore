package minicore.contracts.formaters;

import minicore.contracts.HttpContext;

public interface IOutputFormatter {
    boolean canSupport(HttpContext context);

    String format( );
}
