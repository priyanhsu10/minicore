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

    //Register your service with IOC Container
    @Override
    public void configureServices(IServiceCollection service) {
        service.addSingleton(ITestService.class, TestService.class);
    }
//build your pipeline
    @Override
    public void configure(IApplicationBuilder app) {
        app.use(ExceptionMiddleware.class);
        app.useRouting(); //rout endPoint selector
        //add custom middlewares
        app.useEndpoints();//Endpoint executor
    }

}
