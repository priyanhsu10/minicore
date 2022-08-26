package minicore.contracts.modelbinding;

import minicore.contracts.HttpContext;
import minicore.json.JsonHelper;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class DefaultModelBinder implements  IModelBinder{
    @Override
    public   void bindModel(HttpContext actionContext) {
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

                //read the data from body and
                //currently support of json json bady
                //validate accept header and check support for
                // select supported input formatter
                //if not support then throw exception => not supported

                if (bindingResult.getBodyData() != null && !bindingResult.getBodyData().isEmpty()) {
                    Object data = JsonHelper.deserialize(bindingResult.getBodyData(), p.getType());
                    params.add(data);
                    continue;

                }
                try {
                    params.add(p.getType().newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }


        }
        //   actionContext.getRequest().getRequest
        return params.toArray();
    }
}
