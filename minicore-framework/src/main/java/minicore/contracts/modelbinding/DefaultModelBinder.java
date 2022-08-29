package minicore.contracts.modelbinding;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IFormatProvider;
import minicore.contracts.formaters.IInputFormatter;
import minicore.mvc.filters.MvcException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultModelBinder implements IModelBinder {
    private final IFormatProvider provider;

    public DefaultModelBinder(IFormatProvider provider) {
        this.provider = provider;
    }

    @Override
    public void bindModel(HttpContext context) {
        context.ActionContext.MethodParameters = resolveParameter(context);
    }

    private Object[] resolveParameter(HttpContext httpContext) {
        Method m = httpContext.getEndPointMetadata().ActionMethod;
        if (m.getParameterCount() == 0) {
            return new Object[]{};
        }
        List<Object> params = new ArrayList<>(m.getParameterCount());
        Class<?>[] types = m.getParameterTypes();


        Parameter[] parameters = m.getParameters();
        IModelValueCollector modelValueCollector=new DefaultModelValueCollector(httpContext);
        httpContext.ActionContext.ModelValueCollector=modelValueCollector;
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
            if (httpContext.getRequest().getMethod().equals("POST") ||
                    httpContext.getRequest().getMethod().equals("PUT") ) {
                //validate accept header and check support for

                if (!provider.canSuportedInputMediaType(httpContext.ActionContext.InputMediaType)) {
                    //if not support then throw exception => not supported
                    throw new MvcException("Content type not supported");
                }
                // select supported input formatter
                IInputFormatter formatter = provider.getSuportedInputFormatter(httpContext.ActionContext.InputMediaType);

                Object body=formatter.format(httpContext, p.getType());
                params.add(body);
                continue;

            }
        }


        return params.toArray();
    }
}
