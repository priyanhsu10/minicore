package minicore.host;

public interface IStartup {
public  void  configureServices(IHostBuilder hostBuilder);
public  void configure(IHostBuilder hostBuilder);
}
