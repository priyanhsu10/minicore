package minicore.pipeline;

import minicore.contracts.IMiddleware;
import minicore.contracts.filters.IFilterProvider;
import minicore.contracts.formaters.IFormatProvider;
import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.mvc.IMvcHandler;
import minicore.contracts.mvc.MvcConfigurer;
import minicore.contracts.pipeline.IApplicationBuilder;
import minicore.contracts.results.IResultExectutor;
import minicore.contracts.results.ResultExecutor;
import minicore.ioc.container.Scope;
import minicore.mildlewares.endpointexecuting.EndPointExecutorMiddleware;
import minicore.mildlewares.exceptions.ExceptionMiddleware;
import minicore.mildlewares.routemap.UseRouteingMiddleware;
import minicore.mvc.FilterProvider;
import minicore.mvc.formatters.FormatProvider;
import minicore.mvc.MvcHandler;

import java.util.ArrayList;
import java.util.List;

public class ApplicationBuilder implements IApplicationBuilder {
    private final IServiceCollection serviceCollection;

    public ApplicationBuilder(IServiceCollection serviceCollection) {
        this.serviceCollection = serviceCollection;
        middlewareTypes=new ArrayList<>();
        addInitialMiddlewares();
    }

    private void addInitialMiddlewares() {
        use(ExceptionMiddleware.class);
    }

    private List<Class<? extends IMiddleware>> middlewareTypes;

    @Override
    public IApplicationBuilder use(Class<? extends IMiddleware> middlewareType) {
        middlewareTypes.add(middlewareType);
        serviceCollection.register(middlewareType, Scope.Singleton);
        return this;
    }

    @Override
    public IApplicationBuilder useRouting() {
        use(UseRouteingMiddleware.class);
        return this;
    }

    @Override
    public IApplicationBuilder useEndpoints() {
        use(EndPointExecutorMiddleware.class);
        return this;
    }

    @Override
    public IApplicationBuilder useStaticFiles() {
        return this;
    }

    @Override
    public IApplicationBuilder useAuthentication() {
        return this;
    }

    @Override
    public List<Class<? extends IMiddleware>> getMidlewareTypes() {
        return middlewareTypes;
    }
    private void addMvcServices(IServiceCollection iServiceCollection) {
        iServiceCollection.addSingleton(MvcConfigurer.class,MvcConfigurer.class);
        iServiceCollection.addSingleton(IFilterProvider.class, FilterProvider.class);
        iServiceCollection.addSingleton(IResultExectutor.class, ResultExecutor.class);
        iServiceCollection.addSingleton(IFormatProvider.class, FormatProvider.class);
        iServiceCollection.addTransient(IMvcHandler.class, MvcHandler.class);
    }
}
