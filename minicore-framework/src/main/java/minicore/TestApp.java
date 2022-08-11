package minicore;

import minicore.host.IHostBuilder;
import minicore.host.IStartup;
import minicore.host.WebHostBuilder;

public class TestApp {
    public static void main(String[] args) {
        WebHostBuilder.build(args).useStartup(AppStartup.class).run();
    }
}
class  AppStartup implements IStartup{

    @Override
    public void configureServices(IHostBuilder hostBuilder) {

    }

    @Override
    public void configure(IHostBuilder hostBuilder) {

    }
}