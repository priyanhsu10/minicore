package minicore.contracts;

import minicore.contracts.results.IActionResult;
import minicore.contracts.results.ObjectResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EndPoint {
    public Method ActionMethod;
    public Class<? extends ControllerBase> ControllerClass;
    public String HttpMethod;
    public String[] PatternParameters;
    public String[] UrlTokens;
    public String DisplayName;
    public Map<String, Class<?>> ParameterNameTypes = new HashMap<>();
    public Object[] MethodParameters;
    public String Pattern;
    public boolean isPattern = false;
    public IActionResult actionResult;


    public Class getReturnType() {
        return ActionMethod.getReturnType();
    }

    public Object Invoke(ControllerBase object) {
        return null;
    }

    public EndPoint(Method actionMethod, Class<? extends ControllerBase> controller) {
        ActionMethod = actionMethod;
        this.ControllerClass = controller;

    }

    public void executeAction(Object controller) throws RuntimeException {
        Object result = null;
        try {
            result = ActionMethod.invoke(controller, MethodParameters);

            if (result.getClass().isAssignableFrom(IActionResult.class)) {
                actionResult = (IActionResult) result;
            }
            actionResult = new ObjectResult(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException exception) {
            throw exception;
        }
    }
}