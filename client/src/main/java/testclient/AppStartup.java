package testclient;


import minicore.contracts.host.IStartup;
import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.mvc.MvcConfigurer;
import minicore.contracts.pipeline.IApplicationBuilder;
import minicore.mildlewares.exceptions.ExceptionMiddleware;
import testclient.filters.TestExceptionFilter;
import testclient.filters.TestGlobalActionFilter;
import testclient.filters.TestResultFilter;
import testclient.middlewares.CustomMiddleware;
import testclient.middlewares.TransactionIdMiddleware;
import testclient.services.ITestService;
import testclient.services.TestService;

public class AppStartup implements IStartup {

    //Register your service with IOC Container
    @Override
    public void configureServices(IServiceCollection service) {
        //configuring mvc options
        MvcConfigurer.configureMvc(service,options->{
            // Adding your custom  Global  filters
            options.addGlobalFilter(TestGlobalActionFilter.class);

            //Adding your Custom Exception Filters
            options.addExceptionFilter(TestExceptionFilter.class);
            // Adding your custom Global Result filters
            // Global filter will execute for every action before writing the response
            options.addResultFilter(TestResultFilter.class);

        });
        service.addSingleton(ITestService.class, TestService.class);

    }
//build your pipeline
    @Override
    public void configure(IApplicationBuilder app) {
        app.use(TransactionIdMiddleware.class);
        app.useRouting(); //rout endPoint selector
        //add custom middlewares
        app.use(CustomMiddleware.class);

        app.useEndpoints();//Endpoint executor
    }

}
