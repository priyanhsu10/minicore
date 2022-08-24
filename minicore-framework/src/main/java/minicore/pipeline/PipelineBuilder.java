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

    public PipelineBuilder(IActionDelegate initial, IServiceCollection serviceCollection) {
        this.initialAction = initial;
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


    private IAction createPipeline(int index) {
        if (index < (middlewareTypes.size() - 1)) {
            //create handler and pass context
            IAction actionHandler = createPipeline(index + 1);

            return build(index, actionHandler);
        } else {
            return build(index, null);
        }

    }

    @Override
    public IAction build() {
        return createPipeline(0);
    }

    private IAction build(int index, IAction action) {
        if (action == null) {
            IMiddleware initial = new InitalMidleware();
            return initial::next;
        }
        IMiddleware middleware = middleware = serviceCollection.resolve(middlewareTypes.get(index));
        return middleware::next;

    }
}

class InitalMidleware implements IMiddleware {
    public InitalMidleware() {
    }

    @Override
    public void next(IActionDelegate action, HttpContext httpContext) throws Exception {
        action.invoke(httpContext);
    }
}