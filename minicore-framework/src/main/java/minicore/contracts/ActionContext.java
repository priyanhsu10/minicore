package minicore.contracts;

import minicore.contracts.modelbinding.IModelValueCollector;
import minicore.contracts.results.IActionResult;

public class ActionContext {
    //custom data can keep
    public IActionResult ActionResult;
    public String InputMediaType;
    public String OutputMediaType;
    public Object[] MethodParameters;
    public Object Body;
    public  IModelValueCollector ModelValueCollector;



}
