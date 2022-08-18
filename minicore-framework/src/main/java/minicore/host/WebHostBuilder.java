package minicore.host;

import minicore.contracts.HttpContext;
import minicore.contracts.IAction;
import minicore.endpoints.EndPointManger;
import minicore.ioc.IServiceCollection;
import minicore.ioc.ServiceCollection;
import minicore.mildlewares.exceptions.ExeptionHandlerMiddleware;
import minicore.mildlewares.routemap.UseRouteingMiddleware;
import minicore.pipeline.PipelineBuilder;

public class WebHostBuilder implements IHostBuilder{
    private static IAction action;
    private static EndPointManger endPointManger;
    private static IServiceCollection serviceCollection;
    private  static  PipelineBuilder pipelineBuilder;

    public static IAction getAction() {
        return action;
    }

    public static EndPointManger getEndPointManger() {
        return endPointManger;
    }


    public static IServiceCollection getServiceCollection() {
        return serviceCollection;
    }
    private  WebHostBuilder(String[] args){

    }

    public static PipelineBuilder getPipelineBuilder() {
        return pipelineBuilder;
    }



    @Override
    public void run() {
        //todo: make this configurable
         IServer server= new JettyServer();
         server.start();
    }

    @Override
    public IHostBuilder useStartup(Class<? extends IStartup> startup) {

        try {
            Class startupClass = Class.forName(startup.getTypeName());
            serviceCollection=new ServiceCollection();
            endPointManger = new EndPointManger(startupClass,serviceCollection);
            pipelineBuilder = new PipelineBuilder(WebHostBuilder::initialAction);
            useStartup(startupClass,serviceCollection);
            action = pipelineBuilder.build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return this;
    }
    public void useStartup(Class<? extends IStartup> startupClass, IServiceCollection iServiceCollection) throws Exception {
        IStartup startup = (IStartup) startupClass.getDeclaredConstructor().newInstance();
        startup.configureServices(iServiceCollection);

        addInitialFilters();
        startup.configure(this);

    }

    private static void initialAction(HttpContext httpContext) {
        System.out.println("first middleware");
    }

    public  static WebHostBuilder build(String[] args){

        return  new WebHostBuilder(args);

    }
    private void addInitialFilters() {
        //exception middleware
        pipelineBuilder.use(ExeptionHandlerMiddleware.class);
        //endpoint routing middleware
        pipelineBuilder.use(UseRouteingMiddleware.class);
    }
}
