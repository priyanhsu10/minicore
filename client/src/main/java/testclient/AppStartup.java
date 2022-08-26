package testclient;


import minicore.contracts.host.IStartup;
import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.pipeline.IApplicationBuilder;
import minicore.mildlewares.endpointexecuting.EndPointExecutorMiddleware;
import minicore.mildlewares.exceptions.ExceptionMiddleware;
import minicore.mildlewares.routemap.UseRouteingMiddleware;
import testclient.services.ITestService;
import testclient.services.TestService;

public class AppStartup implements IStartup {

    @Override
    public void configureServices(IServiceCollection service) {
        service.addSingleton(ITestService.class, TestService.class);
    }

    @Override
    public void configure(IApplicationBuilder app) {
        app.use(ExceptionMiddleware.class);
        app.use(UseRouteingMiddleware.class);
        app.use(EndPointExecutorMiddleware.class);
    }

}
