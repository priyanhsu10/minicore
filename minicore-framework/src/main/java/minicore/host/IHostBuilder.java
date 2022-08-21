package minicore.host;

import minicore.ioc.IServiceCollection;
import minicore.pipeline.PipelineBuilder;

public interface IHostBuilder {

    void run();
    IHostBuilder useStartup(Class<? extends  IStartup> startup);
    IServiceCollection services();


}
