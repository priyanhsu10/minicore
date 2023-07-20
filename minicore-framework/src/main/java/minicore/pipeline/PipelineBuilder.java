package minicore.pipeline;

import minicore.contracts.HttpContext;
import minicore.contracts.IActionDelegate;
import minicore.contracts.IMiddleware;
import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.pipeline.IApplicationBuilder;
import minicore.contracts.pipeline.IPipelineBuilder;

import java.util.*;

public class PipelineBuilder implements IPipelineBuilder {
    private List<Class<? extends IMiddleware>> middlewareTypes = new ArrayList<>();
    private IServiceCollection serviceCollection;
    private final IApplicationBuilder appBuilder;

    public PipelineBuilder(IServiceCollection serviceCollection, IApplicationBuilder appBuilder) {

        this.serviceCollection = serviceCollection;
        this.appBuilder = appBuilder;
    }

    private IActionDelegate createPipeline(int index) {
        if (index < (middlewareTypes.size() - 1)) {
            // create handler and pass context
            IActionDelegate actionHandler = createPipeline(index + 1);

            return build(index, actionHandler);
        } else {
            return build(index, null);
        }

    }

    @Override
    public IActionDelegate build() {
        middlewareTypes = appBuilder.getMidlewareTypes();
        return createPipeline(0);
    }

    private IActionDelegate build(int index, IActionDelegate action) {
        if (action == null) {
            action = PipelineBuilder::next;
        }
        IMiddleware middleware = serviceCollection.resolve(middlewareTypes.get(index));
        middleware.setNext(action);
        return middleware::next;

    }

    public static void next(HttpContext httpContext) throws Exception {
        System.out.println("this is initial middleware");
    }
}
