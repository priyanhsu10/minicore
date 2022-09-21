package minicore.contracts.modelbinding;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IFormatProvider;
import minicore.contracts.results.HttpStatus;
import minicore.contracts.results.ModelValidationResult;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Predicate;

public class DefaultModelBinder implements IModelBinder {
    private final IFormatProvider provider;
    private final Validator validator;

    public DefaultModelBinder(IFormatProvider provider) {
        this.provider = provider;
         validator = Validation.byProvider( HibernateValidator.class )
                .configure()
                .failFast( false )
                .buildValidatorFactory()
                .getValidator();

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
        ParameterValueProvider parameterValueProvider = new ParameterValueProvider(modelValueCollector, httpContext, provider);
        Set<ConstraintViolation<Object>> validationResult = new HashSet<>();
        for (Parameter p : parameters) {

            Object value = null;
            for (Map.Entry<Predicate<Parameter>, IValueResolver> entry : parameterValueProvider.getValueResolverMap().entrySet()) {
                if (entry.getKey().test(p)) {
                    value = entry.getValue().resolve(p);
                    break;
                }
            }
            if (value == null && ParameterValueProvider.isPrimitiveType(p.getType())) {
                value = getDefaultValue(p.getType());
            }
            validator.validate(value).stream().forEach(x -> validationResult.add(x));

            params.add(value);


        }

        //from config setting check throw exception or set controller model state
        if (validationResult.size() > 0) {
            httpContext.ActionContext.ModelValidationResult = validationResult;
            //for now returning the badresult with validation message
             ModelError error= new ModelError();
             error.setMessage("Model validation Failed");
            for(ConstraintViolation<Object> objectConstraintViolation:validationResult){
                error.getErrors().put(objectConstraintViolation.getPropertyPath().toString(),objectConstraintViolation.getMessage());
            }
            ModelValidationResult result= new ModelValidationResult(error, HttpStatus.BadRequest);
            httpContext.ActionContext.ActionResult=result;

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
