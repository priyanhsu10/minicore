package minicore.mvc;

import minicore.contracts.ActionContext;
import minicore.contracts.ControllerBase;
import minicore.contracts.HttpContext;
import minicore.contracts.annotations.filters.ActionFilter;
import minicore.contracts.annotations.filters.ResultFilter;
import minicore.contracts.filters.*;
import minicore.contracts.modelbinding.DefaultModelValueCollector;
import minicore.contracts.modelbinding.IModelBinder;
import minicore.contracts.mvc.IMvcHandler;
import minicore.contracts.results.IActionResult;
import minicore.contracts.results.IResultExectutor;
import minicore.contracts.results.ObjectResult;
import minicore.ioc.container.Scope;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MvcHandler implements IMvcHandler {

    private final IModelBinder binder;
    private final IFilterProvider filterProvider;
    private final IResultExectutor resultExectutor;
    private List<IActionFilter> actionFilters = new ArrayList<>();
    private List<IResultExecutionFilter> resultFilters = new ArrayList<>();
    private final List<IAuthenticationFilter> authFilters;
    private final List<IExceptionFilter> exceptionFilters;

    public MvcHandler(IModelBinder binder, IFilterProvider iFilterProvider, IResultExectutor resultExectutor) {


        this.binder = binder;
        this.filterProvider = iFilterProvider;
        this.resultExectutor = resultExectutor;
        actionFilters.addAll(filterProvider.getGlobalFilters());
        this.authFilters = filterProvider.getAuthFilters();
        this.exceptionFilters = filterProvider.getExceptionFilters();
    }

    @Override
    public void process(HttpContext httpContext) {
        prepareActionFilters(httpContext);
        Supplier<Boolean> isResultSet = () -> (httpContext.ActionContext.ActionResult != null);
        //1 . execute  authFilter
        executeAuthFilters(httpContext);

        //auth filter set reusult mean not autherized
        //short-circuiting pipeline
        if (isResultSet.get()) return;

        //2. controller instantiation
        Object c = HttpContext.services.resolve(httpContext.getEndPointMetadata().ControllerClass);
        ((ControllerBase)c).httpContext=httpContext;
        try {
            //3.model binding

            binder.bindModel(httpContext);
            //4.execute before action global filter
            //5.execute controller before action filter
            //6. execute before action filter

            executeFilterBeforeActionExecution(httpContext);
            if (isResultSet.get()) return;

            //7. execute action ()

            try {
                httpContext.ActionContext.ActionResult = executeAction(httpContext, c);

            } catch (RuntimeException e) {
                //set exception on action context
                httpContext.ActionContext.IsActionRaiseException = true;
                httpContext.ActionContext.ActionException = e;
            }
            // 6. execute after action filter
            //5.execute controller after action filter
            // 4.execute after action global filter
            //if action method throws exception then after exception filter not be e
            executeFilterAfterActionExecuted(httpContext);
            //execute  result
            resultExectutor.executeResult(httpContext, getResultFilters(httpContext));

            if (httpContext.ActionContext.IsActionRaiseException) {
                executeExceptionFilters(httpContext, httpContext.ActionContext.ActionException);
            }
            //
        } catch (Exception e) {
      //unhandled exceptions
            throw e;
        }


    }

    public IActionResult executeAction(HttpContext context, Object controller) throws RuntimeException {

        try {
            Object result = context.getEndPointMetadata().ActionMethod.invoke(controller, context.ActionContext.MethodParameters);

            if (result.getClass().isAssignableFrom(IActionResult.class)) {
                return (IActionResult) result;
            } else {
                return new ObjectResult(result);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException exception) {
            throw exception;
        }
    }

    private void executeExceptionFilters(HttpContext context, RuntimeException e) {
        boolean isHanlle = false;
        for (int i = 0; i < exceptionFilters.size(); i++) {
            if (exceptionFilters.get(i).support(e.getClass())) {
                exceptionFilters.get(i).onException(context, e);
                isHanlle = true;
                break;
            }
        }
        if (!isHanlle) {
            throw e;
        }
    }

    private void executeFilterAfterActionExecuted(HttpContext context) {
        for (int i = actionFilters.size() - 1; i >= 0; i--) {

            actionFilters.get(i).afterExecute(context);


        }
    }

    private void executeFilterBeforeActionExecution(HttpContext context) {
        for (int i = 0; i < actionFilters.size(); i++) {

            actionFilters.get(i).beforeExecute(context);
            if (context.ActionContext.ActionResult != null) {
                //auth filter set reusult mean not autherized
                //short-circuiting pipeline
                break;
            }

        }
    }

    private void executeAuthFilters(HttpContext context) {
        for (int i = 0; i < authFilters.size(); i++) {

            authFilters.get(i).onAuthorized(context);
            if (context.ActionContext.ActionResult != null) {
                //auth filter set reusult mean not autherized
                //short-circuiting pipeline
                break;
            }

        }
    }

    private void prepareActionFilters(HttpContext context) {
        List<IActionFilter> controllerActionFilters = getFilters(Arrays.stream(context.getEndPointMetadata().ControllerClass.getDeclaredAnnotations()));
        this.actionFilters.addAll(controllerActionFilters);

        //medhod filter
        List<IActionFilter> methodfilters = getFilters(Arrays.stream(context.getEndPointMetadata().ActionMethod.getDeclaredAnnotations()));
        this.actionFilters.addAll(methodfilters);
    }

    private List<IActionFilter> getFilters(Stream<Annotation> annotationStream) {
        return annotationStream
                .filter(x -> x.annotationType().equals(ActionFilter.class))
                .map(y -> ((ActionFilter)y).filterClass())
                .map(x -> HttpContext.services.tryResolve(x, Scope.Singleton))
                .collect(Collectors.toList());
    }

    private List<IResultExecutionFilter> getResultFilters(HttpContext context) {
        return Arrays.stream(context.getEndPointMetadata().ActionMethod.getDeclaredAnnotations())
                .filter(x -> x.annotationType().equals(ResultFilter.class))
                .map(y -> ((ResultFilter)y).filterClass())
                .map(x -> HttpContext.services.tryResolve(x, Scope.Singleton))
                .collect(Collectors.toList());
    }

}


//}
