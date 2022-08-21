package minicore.host;

import minicore.ioc.IServiceCollection;
import minicore.pipeline.PipelineBuilder;

public interface IStartup {
  void  configureServices(IServiceCollection services);
   void configure(PipelineBuilder hostBuilder);
}
