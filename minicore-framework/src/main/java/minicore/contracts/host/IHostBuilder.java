package minicore.contracts.host;

import minicore.contracts.ioc.IServiceCollection;

public interface IHostBuilder {

    void run();
    IHostBuilder useStartup(Class<? extends IStartup> startup);
    IServiceCollection services();


}
