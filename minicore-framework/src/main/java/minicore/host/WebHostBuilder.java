package minicore.host;

import minicore.contracts.HttpContext;
import minicore.contracts.IAction;
import minicore.contracts.IActionDelegate;
import minicore.contracts.filters.IFilterProvider;
import minicore.contracts.host.IHostBuilder;
import minicore.contracts.host.IServer;
import minicore.contracts.host.IStartup;
import minicore.mvc.FilterProvider;
import minicore.contracts.mvc.IMvcHandler;
import minicore.contracts.mvc.MvcConfigurer;
import minicore.endpoints.EndPointManger;
import minicore.contracts.ioc.IServiceCollection;
import minicore.ioc.ServiceCollection;
import minicore.mvc.MvcHandler;
import minicore.pipeline.PipelineBuilder;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

public class WebHostBuilder implements IHostBuilder {
    private static IActionDelegate action;
    private static EndPointManger endPointManger;
    private static IServiceCollection serviceCollection;
    private  static  PipelineBuilder pipelineBuilder;

    public static IActionDelegate getAction() {
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
            pipelineBuilder = new PipelineBuilder(serviceCollection);
            useStartup(startupClass,serviceCollection);
            action = pipelineBuilder.build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return this;
    }

        @Override
        public IServiceCollection services() {
            return  serviceCollection;
        }

        public void useStartup(Class<? extends IStartup> startupClass, IServiceCollection iServiceCollection) throws Exception {
        IStartup startup = (IStartup) startupClass.getDeclaredConstructor().newInstance();
        registerInitialServices(iServiceCollection);
        startup.configureServices(iServiceCollection);
        

        addInitialFilters();
        startup.configure(pipelineBuilder);

    }

        private void registerInitialServices(IServiceCollection iServiceCollection) {
        iServiceCollection.addSingleton(IServiceCollection.class,()->iServiceCollection);
        iServiceCollection.addSingleton(MvcConfigurer.class,MvcConfigurer.class);
        iServiceCollection.addSingleton(IFilterProvider.class, FilterProvider.class);
        iServiceCollection.addTransient(IMvcHandler.class, MvcHandler.class);
        iServiceCollection.addSingleton(ILoggerFactory.class, ()-> LoggerFactory.getILoggerFactory());
//        iServiceCollection.addTransient(Logger.class, ()-> LoggerFactory.getILoggerFactory().getLogger());
        //wherever resolver needed in the pipeline it is present
          HttpContext.services= iServiceCollection;
        }

        private static void initialAction(HttpContext httpContext) {
        System.out.println("before first middleware");

        System.out.println("after first middleware");
    }

    public  static WebHostBuilder build(String[] args){

        return  new WebHostBuilder(args);

    }
    private void addInitialFilters() {
       //add initial configuration middleware
    }
}
