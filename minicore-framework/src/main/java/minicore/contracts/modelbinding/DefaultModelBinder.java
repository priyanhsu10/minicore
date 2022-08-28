package minicore.contracts.modelbinding;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IFormatProvider;
import minicore.contracts.formaters.IInputFormatter;
import minicore.json.JsonHelper;
import minicore.mvc.filters.MvcException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class DefaultModelBinder implements IModelBinder {
    private final IFormatProvider provider;

    public DefaultModelBinder(IFormatProvider provider) {
        this.provider = provider;
    }

    @Override
    public void bindModel(HttpContext actionContext) {
        actionContext.getEndpoint().MethodParameters = resolveParameter(actionContext);
    }

    private Object[] resolveParameter(HttpContext actionContext) {
        Method m = actionContext.getEndpoint().ActionMethod;
        if (m.getParameterCount() == 0) {
            return new Object[]{};
        }
        List<Object> params = new ArrayList<>(m.getParameterCount());
        Class<?>[] types = m.getParameterTypes();
        DefaultModelValueCollector bindingResult = new DefaultModelValueCollector(actionContext);

        Parameter[] parameters = m.getParameters();
//  todo: apply  attribute from query ,from route ,form body
        for (Parameter p : parameters) {

            if (bindingResult.getRouteData().containsKey(p.getName())) {

                params.add(bindingResult.getRouteData().get(p.getName()));
                continue;
            }
            if (bindingResult.getQueryParameters().containsKey(p.getName())) {
                params.add(bindingResult.getQueryParameters().get(p.getName()));
                continue;
            }
            if (actionContext.getRequest().getMethod().equals("POST") ||
                    actionContext.getRequest().getMethod().equals("PUT")) {
                //validate accept header and check support for
                if (!provider.canSuportedInputMediaType(actionContext.getEndpoint().InputMediaType)) {
                    //if not support then throw exception => not supported
                    throw new MvcException("Content type not supported");
                }
                // select supported input formatter
                IInputFormatter formatter = provider.getSuportedInputFormatter(actionContext);
                params.add(formatter.format(actionContext, p.getType()));
                continue;

            }
        }


        return params.toArray();
    }
}
