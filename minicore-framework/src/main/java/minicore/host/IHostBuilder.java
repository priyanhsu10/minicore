package minicore.host;

public interface IHostBuilder {

    void run();
    IHostBuilder useStartup(Class<? extends  IStartup> startup);
}
