package minicore.contracts;

import minicore.contracts.modelbinding.IModelValueCollector;
import minicore.contracts.results.IActionResult;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class ActionContext {
    //custom data can keep
    public IActionResult ActionResult;
    public String InputMediaType;
    public String OutputMediaType;
    public Object[] MethodParameters;
    public Set<ConstraintViolation<Object>> ModelValidationResult;
    public Object Body;
    public  IModelValueCollector ModelValueCollector;
    public RuntimeException ActionException;


    public boolean IsActionRaiseException;
}
