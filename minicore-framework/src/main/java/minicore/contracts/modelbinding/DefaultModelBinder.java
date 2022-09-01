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

            Object value=null;
            for (Map.Entry<Predicate<Parameter>, IValueResolver> entry : parameterValueProvider.getValueResolverMap().entrySet()) {
                if (entry.getKey().test(p)) {
                     value=entry.getValue().resolve(p);
                    break;
                }
            }
            if(value==null && ParameterValueProvider.isPrimitiveType(p.getType())){
                value= getDefaultValue(p.getType());
            }
            params.add(value);


        }


        return params.toArray();
    }

    private Object getDefaultValue(Class<?> parameterType) {

        if (parameterType.isAssignableFrom(String.class)) {
            return null;
        }
        if (parameterType.isAssignableFrom(Integer.class) || parameterType.isAssignableFrom(int.class)) {
            return 0;
        }
        if (parameterType.isAssignableFrom(Double.class) || parameterType.isAssignableFrom(double.class)) {
            return 0.0;
        }
        if (parameterType.isAssignableFrom(Boolean.class) || parameterType.isAssignableFrom(boolean.class)) {
            return false;
        }
        return null;
    }
}
