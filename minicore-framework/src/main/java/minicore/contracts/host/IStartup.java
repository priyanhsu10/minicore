package minicore.contracts.host;

import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.pipeline.IApplicationBuilder;

public interface IStartup {
  void  configureServices(IServiceCollection services);
   void configure(IApplicationBuilder app);
}
