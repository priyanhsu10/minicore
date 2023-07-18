#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};


import minicore.contracts.host.IStartup;
import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.mvc.MvcConfigurer;
import minicore.contracts.pipeline.IApplicationBuilder;
import ${package}.filters.TestExceptionFilter;
import ${package}.filters.TestGlobalActionFilter;
import ${package}.filters.TestResultFilter;
import ${package}.middlewares.CustomMiddleware;
import ${package}.middlewares.TransactionIdMiddleware;
import ${package}.services.*;

public class AppStartup implements IStartup {

    //Register your service with IOC Container
    @Override
    public void configureServices(IServiceCollection service) {
        //configuring mvc options
        MvcConfigurer.configureMvc(service, options -> {
            // Adding your custom  Global  filters
            options.addGlobalFilter(TestGlobalActionFilter.class);

            //Adding your Custom Exception Filters
            options.addExceptionFilter(TestExceptionFilter.class);
            // Adding your custom Global Result filters
            // Global filter will execute for every action before writing the response
            options.addResultFilter(TestResultFilter.class);

        });
        service.addSingleton(ITodoService.class, TodoService.class);


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
