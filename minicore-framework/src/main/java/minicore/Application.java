package minicore;

import minicore.contracts.host.IStartup;
import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.mvc.MvcConfigurer;
import minicore.contracts.pipeline.IApplicationBuilder;

public class Application {
    public static void main(String[] args) {

    }

    class  Startup implements IStartup{


        @Override
        public void configureServices(IServiceCollection services) {
            MvcConfigurer.configureMvc(services, (option)->{


           });
        }

        @Override
        public void configure(IApplicationBuilder app) {


        }
    }
}
