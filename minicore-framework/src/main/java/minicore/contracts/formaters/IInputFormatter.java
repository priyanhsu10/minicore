package minicore.contracts.formaters;

import minicore.contracts.HttpContext;

public interface IInputFormatter {
    boolean canSupport(HttpContext context);

    Object format(HttpContext context);
}
