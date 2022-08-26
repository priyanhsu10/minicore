package minicore.mvc;

import minicore.contracts.HttpContext;
import minicore.contracts.filters.*;
import minicore.contracts.modelbinding.IModelBinder;
import minicore.contracts.mvc.IMvcHandler;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MvcHandler implements IMvcHandler {

    private final IModelBinder binder;
    private final IFilterProvider filterProvider;
    private List<IActionFilter> actionFilters = new ArrayList<>();
    private final List<IAuthenticationFilter> authFilters ;
    private final List<IExceptionFilter> exceptionFilters ;
    public MvcHandler(IModelBinder binder,IFilterProvider iFilterProvider) {


        this.binder = binder;
        this.filterProvider = iFilterProvider;
        actionFilters.addAll(filterProvider.getGlobalFilters());
        this.authFilters=filterProvider.getAuthFilters();
        this.exceptionFilters= filterProvider.getexceptionFilters();
    }

    @Override
    public void process(HttpContext context) {
        prepareActionFilters(context);
        Supplier<Boolean> isResultSet=()->(context.getEndpoint().actionResult != null);
        //1 . execute  authFilter
        executeAuthFilters(context);

        //auth filter set reusult mean not autherized
        //short-circuiting pipeline
        if (isResultSet.get()) return;

        //2. controller instantiation
        Object c = HttpContext.services.resolve(context.getEndpoint().ControllerClass);

        //3.model binding

        try {
            binder.bindModel(context);
            //4.execute before action global filter
            //5.execute controller before action filter
            //6. execute before action filter
            executeFilterBeforeActionExecution(context);
            if (isResultSet.get()) return;

            //7. execute action ()
             context.getEndpoint().executeAction(c);
            // 6. execute after action filter
            //5.execute controller after action filter
            // 4.execute after action global filter
            executeFilterAfterActionExecuted(context);



            //
        } catch (RuntimeException e) {

            // execute exception filters

            executeExceptionFilters(context, e);
        }


    }

    private void executeExceptionFilters(HttpContext context, RuntimeException e) {
        for (int i=0;i<exceptionFilters.size();i++){
            if(exceptionFilters.get(i).support(e.getClass())){
                exceptionFilters.get(i).onException(context, e);
                break;
            }
        }
    }

    private void executeFilterAfterActionExecuted(HttpContext context) {
        for (int i = actionFilters.size()-1; i >=0; i++) {

            actionFilters.get(i).afterExecute(context);


        }
    }

    private void executeFilterBeforeActionExecution(HttpContext context) {
        for (int i = 0; i < actionFilters.size(); i++) {

            actionFilters.get(i).beforeExecute(context);
            if (context.getEndpoint().actionResult != null) {
                //auth filter set reusult mean not autherized
                //short-circuiting pipeline
                break;
            }

        }
    }

    private void executeAuthFilters(HttpContext context) {
        for (int i = 0; i <  authFilters.size(); i++) {

            authFilters.get(i).onAuthorized(context);
            if (context.getEndpoint().actionResult != null) {
                //auth filter set reusult mean not autherized
                //short-circuiting pipeline
                break;
            }

        }
    }

    private void prepareActionFilters(HttpContext context) {
        List<IActionFilter> controllerActionFilters = getFilters(Arrays.stream(context.getEndpoint().ControllerClass.getDeclaredAnnotations()));
        this.actionFilters.addAll(controllerActionFilters);

        //medhod filter
        List<IActionFilter> methodfilters = getFilters(Arrays.stream(context.getEndpoint().ActionMethod.getDeclaredAnnotations()));
        this.actionFilters.addAll(methodfilters);

    }

    private List<IActionFilter> getFilters(Stream<Annotation> annotationStream) {
        return annotationStream
                .filter(x -> x.annotationType().isAnnotationPresent(ActionFilter.class))
                .map(x -> x.annotationType().getAnnotation(ActionFilter.class).filterClass())
                .map(x -> HttpContext.services.resolve(x))
                .collect(Collectors.toList());
    }

}


//}
