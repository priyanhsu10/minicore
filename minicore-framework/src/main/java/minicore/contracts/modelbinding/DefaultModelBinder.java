package minicore.contracts.modelbinding;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IFormatProvider;
import minicore.contracts.formaters.IInputFormatter;
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
    public void bindModel(HttpContext context) {
        context.ActionContext.MethodParameters = resolveParameter(context);
    }

    private Object[] resolveParameter(HttpContext context) {
        Method m = context.getEndPointMetadata().ActionMethod;
        if (m.getParameterCount() == 0) {
            return new Object[]{};
        }
        List<Object> params = new ArrayList<>(m.getParameterCount());
        Class<?>[] types = m.getParameterTypes();


        Parameter[] parameters = m.getParameters();
        IModelValueCollector modelValueCollector=new DefaultModelValueCollector(context);
        context.ActionContext.ModelValueCollector=modelValueCollector;
//  todo: apply  attribute from query ,from route ,form body
        for (Parameter p : parameters) {

            if (modelValueCollector.getRouteData().containsKey(p.getName())) {

                params.add(modelValueCollector.getRouteData().get(p.getName()));
                continue;
            }
            if (modelValueCollector.getQueryParameters().containsKey(p.getName())) {
                params.add(modelValueCollector.getQueryParameters().get(p.getName()));
                continue;
            }
            if (context.getRequest().getMethod().equals("POST") ||
                    context.getRequest().getMethod().equals("PUT")) {
                //validate accept header and check support for
                if (!provider.canSuportedInputMediaType(context.ActionContext.InputMediaType)) {
                    //if not support then throw exception => not supported
                    throw new MvcException("Content type not supported");
                }
                // select supported input formatter
                IInputFormatter formatter = provider.getSuportedInputFormatter(context);
                params.add(formatter.format(context, p.getType()));
                continue;

            }
        }


        return params.toArray();
    }
}
