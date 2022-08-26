package minicore.mildlewares.endpointexecuting;


import minicore.contracts.HttpContext;
import minicore.contracts.IActionDelegate;
import minicore.contracts.IMiddleware;
import minicore.contracts.mvc.IMvcHandler;

public class EndPointExecutorMiddleware implements IMiddleware {

    private  IActionDelegate action;

    @Override
    public void setNext(IActionDelegate action) {
        this.action=action;
    }
    @Override
    public void next(HttpContext actionContext) throws Exception {
        //per request
        IMvcHandler mvcHandler = HttpContext.services.resolve(IMvcHandler.class);

        mvcHandler.process(actionContext);

    }


//    private void writeResponse(HttpContext actionContext, Object processResult) throws IOException {
//
//        actionContext.getResponse().addHeader("Content-Type", "application/json");
//        if(processResult==null){
//            actionContext.getResponse().setStatus(204);
//            return;
//        }
//        actionContext.getResponse().getWriter().println(processResult);
//    }
//
//    private Object ProcessResult(Object result, HttpContext actionContext) {
//        String contentType = actionContext.getRequest().getHeader("Content-Type");
//        if (result.getClass().equals(String.class)) {
//            return result;
//        }
//        if (result.getClass().equals(Void.class)) {
//            return "";
//        }
//        if (contentType == null || contentType.equals("application/json")) {
//            return JsonHelper.serialize(result, result.getClass());
//        }
//        //todo: handle xml result
//
//        if (contentType.equals("application/xml")) {
////            JAXBContext jaxbContext = JAXBContext.newInstance(actionContext.getEndpoint().getReturnType());
////            Unmarshaller jaxbUnmarshaller = jaxbContext.createMarshaller();
////            Tutorials tutorials = (Tutorials) jaxbUnmarshaller.unmarshal(this.getFile());
//            //todo: for now returning json but return xml
//            return JsonHelper.serialize(result, result.getClass());
//        }
//        //todo: handle file result
//        return "";
//    }
//
//    private Object[] resolveParameter(HttpContext actionContext) throws InstantiationException, IllegalAccessException {
//        Method m = actionContext.getEndpoint().ActionMethod;
//        if (m.getParameterCount() == 0) {
//            return new Object[]{};
//        }
//        List<Object> params = new ArrayList<>(m.getParameterCount());
//        Class<?>[] types = m.getParameterTypes();
//        DefaultModelValueCollector bindingResult = new DefaultModelValueCollector(actionContext);
//
//        Parameter[] parameters = m.getParameters();
//
//        for (Parameter p : parameters) {
//
//            if (bindingResult.getRouteData().containsKey(p.getName())) {
//
//                params.add(bindingResult.getRouteData().get(p.getName()));
//                continue;
//            }
//            if (bindingResult.getQueryParameters().containsKey(p.getName())) {
//                params.add(bindingResult.getQueryParameters().get(p.getName()));
//                continue;
//            }
//            if (actionContext.getRequest().getMethod().equals("POST") ||
//                    actionContext.getRequest().getMethod().equals("PUT")) {
//                //read the data from body and
//                //currently support of json json bady
//                //validate accept header and check support for
//                // select supported input formatter
//                //if not support then throw exception => not supported
//
//                if (bindingResult.getBodyData() != null && !bindingResult.getBodyData().isEmpty()) {
//                    Object data = JsonHelper.deserialize(bindingResult.getBodyData(), p.getType());
//                    params.add(data);
//                    continue;
//
//                }
//                params.add(p.getType().newInstance());
//            }
//
//
//        }
//        //   actionContext.getRequest().getRequest
//        return params.toArray();
//    }
}
