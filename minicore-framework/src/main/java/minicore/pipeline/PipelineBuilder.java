package minicore.pipeline;

import minicore.contracts.HttpContext;
import minicore.contracts.IAction;
import minicore.contracts.IActionDelegate;
import minicore.contracts.IMiddleware;
import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.pipeline.IApplicationBuilder;
import minicore.ioc.container.Scope;

import java.util.*;

public class PipelineBuilder implements IApplicationBuilder {
    private IActionDelegate initialAction;
    public List<Class<? extends IMiddleware>> middlewareTypes = new ArrayList<>();
    private Map<String, IAction> actionMapList = new HashMap<>();
    private IServiceCollection serviceCollection;

    public PipelineBuilder(IServiceCollection serviceCollection) {

        this.serviceCollection = serviceCollection;
    }

    @Override
    public IApplicationBuilder use(Class<? extends IMiddleware> middlewareType) {
        middlewareTypes.add(middlewareType);
        serviceCollection.register(middlewareType, Scope.Singleton);
        return this;
    }

    @Override
    public IApplicationBuilder map(String url, IAction action) {
        actionMapList.put(url, action);
        return this;
    }


    private IActionDelegate createPipeline(int index) {
        if (index < (middlewareTypes.size() - 1)) {
            //create handler and pass context
            IActionDelegate actionHandler = createPipeline(index + 1);

            return build(index, actionHandler);
        } else {
            return build(index, null);
        }

    }

    @Override
    public IActionDelegate build() {
        return createPipeline(0);
    }

    private IActionDelegate build(int index, IActionDelegate action) {
        if (action == null) {
            IMiddleware initial = new InitalMidleware();
            initial.setNext(action);
            return initial::next ;
        }
        IMiddleware middleware = middleware = serviceCollection.resolve(middlewareTypes.get(index));
        middleware.setNext(action);
        return middleware::next;

    }
}

class InitalMidleware implements IMiddleware {
    IActionDelegate action;
    public InitalMidleware() {
    }
    @Override
    public void setNext(IActionDelegate action) {
        this.action=action;
    }

    @Override
    public void next(HttpContext httpContext) throws Exception {

    }
}