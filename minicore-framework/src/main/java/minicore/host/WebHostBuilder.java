package minicore.host;

public class WebHostBuilder implements IHostBuilder{

    private  WebHostBuilder(String[] args){

    }
    @Override
    public void run() {
        //todo: make this configurable
         IServer server= new JettyServer();
         server.start();
    }

    @Override
    public IHostBuilder useStartup(Class<? extends IStartup> startup) {
        return this;
    }

    public  static WebHostBuilder build(String[] args){

        return  new WebHostBuilder(args);

    }
}
