package minicore.contracts.modelbinding;

import minicore.contracts.EndPointMetadata;
import minicore.contracts.HttpContext;
import minicore.contracts.HttpMethod;
import minicore.contracts.annotations.modelBinding.*;
import minicore.contracts.formaters.IFormatProvider;
import minicore.contracts.formaters.IInputFormatter;
import minicore.mvc.filters.MvcException;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Predicate;

public class ParameterValueProvider {

    private final IModelValueCollector collector;
    private final HttpContext httpContext;
    private final IFormatProvider provider;
    public static List<Class<?>> primitiveTypes = new ArrayList<>();

    static {
        primitiveTypes.add(int.class);
        primitiveTypes.add(Integer.class);
        primitiveTypes.add(String.class);
        primitiveTypes.add(Boolean.class);
        primitiveTypes.add(boolean.class);
        primitiveTypes.add(float.class);
        primitiveTypes.add(Float.class);
        primitiveTypes.add(Double.class);
        primitiveTypes.add(double.class);
        primitiveTypes.add(long.class);
        primitiveTypes.add(Long.class);
        primitiveTypes.add(short.class);
        primitiveTypes.add(Short.class);
        primitiveTypes.add(byte.class);
        primitiveTypes.add(Byte.class);


    }

    public Map<Predicate<Parameter>, IValueResolver> getValueResolverMap() {
        return valueResolverMap;
    }

    private Map<Predicate<Parameter>, IValueResolver> valueResolverMap;

    public ParameterValueProvider(IModelValueCollector collector, HttpContext httpContext, IFormatProvider provider) {
        this.collector = collector;
        this.httpContext = httpContext;
        this.provider = provider;
        valueResolverMap = new LinkedHashMap<>();
        fillDictionary();
    }

    private void fillDictionary() {

        valueResolverMap.put(isFromBody(), this::parameterValueFromBody);
        valueResolverMap.put(isFromForm(), this::valueFromFrom);

        valueResolverMap.put(getParameterPredicate(), this::valueFromRoute);
        valueResolverMap.put(p -> isPrimitiveType(p.getType()) && p.isAnnotationPresent(FromHeader.class), this::valueFromHeader);
        valueResolverMap.put(p -> isPrimitiveType(p.getType()) && collector.getQueryParameters().containsKey(p.getName()), this::valueFromQuery);
        valueResolverMap.put(isCustomObjectContainsModelBindingAttr(), this::CustomObjectContainsModelBindingAttr);
        valueResolverMap.put(p -> p.isAnnotationPresent(FromFile.class), this::valueFromFile);

    }

    private Predicate<Parameter> isFromForm() {
        return p -> p.isAnnotationPresent(FromForm.class) &&
                (httpContext.getEndPointMetadata().HttpMethod.equals(HttpMethod.POST)
                        || httpContext.getEndPointMetadata().HttpMethod.equals(HttpMethod.PUT));
    }

    private Object valueFromFile(Parameter parameter) {
        throw new RuntimeException("File handling Not supported currently");
    }

    private Predicate<Parameter> getParameterPredicate() {
        return p -> p.isAnnotationPresent(FromRoute.class) || collector.getRouteData().containsKey(p.getName()) || httpContext.getEndPointMetadata().isPattern;
    }

    public Object valueFromRoute(Parameter p) {

        return collector.getRouteData().get(p.getName());
    }

    public Object valueFromQuery(Parameter p) {
        return collector.getQueryParameters().containsKey(p.getName()) ? collector.getQueryParameters().get(p.getName()) : createInstance(p.getType());

    }

    private Predicate<Parameter> isFromBody() {
        return p -> {
            boolean isPutOrPost = isPutOrPost();
            return isPutOrPost && p.isAnnotationPresent(FromBody.class);
        };

    }

    private boolean isPutOrPost() {
        return httpContext.getRequest().getMethod().equals("POST") || httpContext.getRequest().getMethod().equals("PUT");
    }

    private Predicate<Parameter> isCustomObjectContainsModelBindingAttr() {
        return p -> {


            boolean isCustomObject = !ParameterValueProvider.isPrimitiveType(p.getType()) &&
                    !p.isAnnotationPresent(FromBody.class) &&
                    !p.isAnnotationPresent(FromRoute.class) &&
                    !p.isAnnotationPresent(FromFile.class) &&
                    !p.isAnnotationPresent(FromForm.class) &&
                    !p.isAnnotationPresent(FromHeader.class) &&
                    !p.isAnnotationPresent(FromQuery.class);


            boolean canProcess = isCustomObject && Arrays.stream(p.getType().getDeclaredFields()).anyMatch(f ->
                    f.isAnnotationPresent(FromRoute.class) || f.isAnnotationPresent(FromQuery.class) ||
                            f.isAnnotationPresent(FromHeader.class) ||
                            f.isAnnotationPresent(FromBody.class));

            return canProcess;

        };

    }

    private Object CustomObjectContainsModelBindingAttr(Parameter p) {

        try {
            Object parameter = createInstance(p.getType());
            for (Field f : p.getType().getDeclaredFields()) {
                f.setAccessible(true);

                if (f.isAnnotationPresent(FromHeader.class) && collector.getHeaders().containsKey(f.getName())) {
                    f.set(parameter, collector.getHeaders().get(f.getName()));
                    continue;
                }
                if (f.isAnnotationPresent(FromQuery.class) && collector.getQueryParameters().containsKey(f.getName())) {
                    f.set(parameter, collector.getQueryParameters().get(f.getName()));
                    continue;
                }
                if (f.isAnnotationPresent(FromRoute.class) && collector.getRouteData().containsKey(f.getName())) {
                    f.set(parameter, collector.getRouteData().get(f.getName()));
                    continue;
                }
                if (f.isAnnotationPresent(FromBody.class) && isPutOrPost() && !isPrimitiveType(f.getType())) {

                    f.set(parameter, valueFromBody(f.getType()));
                    continue;
                }


            }
            return parameter;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Object parameterValueFromBody(Parameter p) {

        return valueFromBody(p.getType());
    }

    public Object valueFromBody(Class<?> p) {


        //validate accept header and check support for
        if (!provider.canSuportedInputMediaType(httpContext.ActionContext.InputMediaType)) {
            //if not support then throw exception => not supported
            throw new MvcException("Content type not supported");
        }
        // select supported input formatter
        IInputFormatter formatter = provider.getSuportedInputFormatter(httpContext.ActionContext.InputMediaType);

        Object body = formatter.format(httpContext, p);
        return body;
    }

    //this section should be tested thoroughly with list type parameters
    public Object valueFromFrom(Parameter p) {
        try {
            Object parameter = createInstance(p.getType());
            for (Field f : p.getType().getDeclaredFields()) {
                f.setAccessible(true);
                if (collector.getFormData().containsKey(f.getName())) {
                    String[] values = collector.getFormData().get(f.getName());
                    if (values == null || values.length == 0) {
                        continue;
                    }
                    if (f.getType().isAssignableFrom(List.class)) {
                        ArrayList fieldValues = new ArrayList();

                        for (String v : values) {

                            fieldValues.add(v);
                        }
                        f.set(parameter, fieldValues);

                    } else {
                        if (values.length > 0) {
                            parameter = f.getType().cast(values[0]);
                        }
                    }
                }


            }
            return parameter;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Object createInstance(Class<?> p) {
        try {
            return p.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Object valueFromHeader(Parameter p) {
        return collector.getHeaders().containsKey(p.getName()) ? collector.getHeaders().get(p.getAnnotation(FromHeader.class).Key()) : createInstance(p.getType());
    }

    public static boolean isPrimitiveType(Class<?> parameterType) {
        return primitiveTypes.contains(parameterType);

    }
}
