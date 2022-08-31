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
import java.util.Map;
import java.util.function.Predicate;
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
        IModelValueCollector modelValueCollector = new DefaultModelValueCollector(httpContext);
        httpContext.ActionContext.ModelValueCollector = modelValueCollector;
        ParameterValueProvider parameterValueProvider = new ParameterValueProvider(modelValueCollector, httpContext,provider);
        for (Parameter p : parameters) {

            for (Map.Entry<Predicate<Parameter>, IValueResolver> entry : parameterValueProvider.getValueResolverMap().entrySet()) {
                if (entry.getKey().test(p)) {
                    params.add(entry.getValue().resolve(p));
                    break;
                }
            }

        }


        return params.toArray();
    }
}
