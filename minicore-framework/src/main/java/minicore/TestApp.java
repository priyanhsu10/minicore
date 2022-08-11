package minicore;

import minicore.host.IHostBuilder;
import minicore.host.IStartup;
import minicore.host.WebHostBuilder;
import minicore.ioc.IServiceCollection;

public class TestApp {
    public static void main(String[] args) {
        WebHostBuilder.build(args).useStartup(AppStartup.class).run();
    }
}
class  AppStartup implements IStartup{

    @Override
    public void configureServices(IServiceCollection app) {

    }

    @Override
    public void configure(IHostBuilder app) {

    }
}