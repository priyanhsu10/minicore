package minicore.host;

import minicore.ioc.IServiceCollection;

public interface IStartup {
  void  configureServices(IServiceCollection services);
   void configure(IHostBuilder hostBuilder);
}
