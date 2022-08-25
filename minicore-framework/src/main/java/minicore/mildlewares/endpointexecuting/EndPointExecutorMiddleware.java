package minicore.mildlewares.endpointexecuting;


import minicore.contracts.HttpContext;
import minicore.contracts.IActionDelegate;
import minicore.contracts.IMiddleware;
import minicore.contracts.modelbinding.DefaultModelValueCollector;
import minicore.host.WebHostBuilder;
import minicore.json.JsonHelper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class EndPointExecutorMiddleware implements IMiddleware {

    public EndPointExecutorMiddleware() {
    }

    @Override
    public void next(IActionDelegate action, HttpContext actionContext) throws Exception{


        //1. controller initialization
        Object c = WebHostBuilder.getServiceCollection().resolve(actionContext.getEndpoint().ControllerClass);
        //2. Model Binding
        Object[] parameter = resolveParameter(actionContext);

        //todo:2. Model validation

        //todo:basically prepare pipeline for filters same as middleware

        // todo: filter execution (in next phase)

        //action execution
        Object result = actionContext.getEndpoint().ActionMethod.invoke(c, parameter);
        //result filter execution

        actionContext.setActionResult(result);

        //5.result processing
        if (result != null) {

            Object processResult = ProcessResult(result, actionContext);
            actionContext.setActionResult(processResult);
            writeResponse(actionContext, processResult);
        } else {
            writeResponse(actionContext,null );
        }


    }

    private void writeResponse(HttpContext actionContext, Object processResult) throws IOException {

        actionContext.getResponse().addHeader("Content-Type", "application/json");
        if(processResult==null){
            actionContext.getResponse().setStatus(204);
            return;
        }
        actionContext.getResponse().getWriter().println(processResult);
    }

    private Object ProcessResult(Object result, HttpContext actionContext) {
        String contentType = actionContext.getRequest().getHeader("Content-Type");
        if (result.getClass().equals(String.class)) {
            return result;
        }
        if (result.getClass().equals(Void.class)) {
            return "";
        }
        if (contentType == null || contentType.equals("application/json")) {
            return JsonHelper.serialize(result, result.getClass());
        }
        //todo: handle xml result

        if (contentType.equals("application/xml")) {
//            JAXBContext jaxbContext = JAXBContext.newInstance(actionContext.getEndpoint().getReturnType());
//            Unmarshaller jaxbUnmarshaller = jaxbContext.createMarshaller();
//            Tutorials tutorials = (Tutorials) jaxbUnmarshaller.unmarshal(this.getFile());
            //todo: for now returning json but return xml
            return JsonHelper.serialize(result, result.getClass());
        }
        //todo: handle file result
        return "";
    }

    private Object[] resolveParameter(HttpContext actionContext) throws InstantiationException, IllegalAccessException {
        Method m = actionContext.getEndpoint().ActionMethod;
        if (m.getParameterCount() == 0) {
            return new Object[]{};
        }
        List<Object> params = new ArrayList<>(m.getParameterCount());
        Class<?>[] types = m.getParameterTypes();
        DefaultModelValueCollector bindingResult = new DefaultModelValueCollector(actionContext);

        Parameter[] parameters = m.getParameters();

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
                params.add(p.getType().newInstance());
            }


        }
        //   actionContext.getRequest().getRequest
        return params.toArray();
    }
}
