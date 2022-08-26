package minicore.contracts.modelbinding;

import minicore.contracts.HttpContext;

public interface IModelBinder {

    void bindModel(HttpContext context);
}
