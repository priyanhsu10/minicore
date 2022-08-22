package minicore;

import minicore.contracts.host.IStartup;
import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.pipeline.IApplicationBuilder;
import minicore.mildlewares.exceptions.ExceptionMiddleware;
import minicore.pipeline.PipelineBuilder;

public class Application {
    public static void main(String[] args) {

    }

    class  Startup implements IStartup{


        @Override
        public void configureServices(IServiceCollection services) {

        }

        @Override
        public void configure(IApplicationBuilder app) {
            app.map("/test",httpContext -> {
               httpContext.getResponse()
                       .getWriter()
                       .println("this is from action");
            });

        }
    }
}
