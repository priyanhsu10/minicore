package testclient;

import minicore.host.IStartup;
import minicore.ioc.IServiceCollection;
import minicore.mildlewares.endpointexecuting.EndPointExecutorMiddleware;
import minicore.mildlewares.exceptions.ExceptionMiddleware;
import minicore.mildlewares.routemap.UseRouteingMiddleware;
import minicore.pipeline.PipelineBuilder;
import testclient.services.ITestService;
import testclient.services.TestService;

public class AppStartup implements IStartup {

    @Override
    public void configureServices(IServiceCollection service) {
        service.addSingleton(ITestService.class, TestService.class);
    }

    @Override
    public void configure(PipelineBuilder app) {

        app.use(ExceptionMiddleware.class);
        app.use(UseRouteingMiddleware.class);
        app.use(EndPointExecutorMiddleware.class);

    }
}
