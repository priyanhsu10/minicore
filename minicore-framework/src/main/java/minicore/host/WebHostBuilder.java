package minicore.host;

import minicore.configuration.AppConfigurer;
import minicore.configuration.ConfigurationReader;
import minicore.configuration.IAppConfigure;
import minicore.contracts.IActionDelegate;
import minicore.contracts.host.IHostBuilder;
import minicore.contracts.host.IServer;
import minicore.contracts.host.IStartup;
import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.pipeline.IApplicationBuilder;
import minicore.contracts.pipeline.IPipelineBuilder;
import minicore.endpoints.EndPointManger;
import minicore.ioc.ServiceCollection;
import minicore.mvc.MveHelper;
import minicore.pipeline.ApplicationBuilder;
import minicore.pipeline.PipelineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebHostBuilder implements IHostBuilder {
    private static IActionDelegate action;
    private static EndPointManger endPointManger;
    private static IServiceCollection serviceCollection;
    private  static IPipelineBuilder pipelineBuilder;
    private  static IApplicationBuilder applicationBuilder;
    private final String[] args;
    private  ConfigurationReader reader;

    public WebHostBuilder(String[] args, ClassLoader classLoader) {
        this.args=args;
        reader= new ConfigurationReader(classLoader);
    }

    public static IActionDelegate getAction() {
        return action;
    }
    private static Logger logger = LoggerFactory.getLogger(WebHostBuilder.class);
    public static EndPointManger getEndPointManger() {
        return endPointManger;
    }


    public static IServiceCollection getServiceCollection() {
        return serviceCollection;
    }
    private  WebHostBuilder(String[] args){

        this.args = args;
    }

    public static IPipelineBuilder getPipelineBuilder() {
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
            applicationBuilder= new ApplicationBuilder(serviceCollection);
            pipelineBuilder = new PipelineBuilder(serviceCollection,applicationBuilder);
            useStartup(startupClass,serviceCollection);
            action = pipelineBuilder.build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(),e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(),e);
            throw new RuntimeException(e);
        }


        return this;
    }

        @Override
        public IServiceCollection services() {
            return  serviceCollection;
        }

        public void useStartup(Class<? extends IStartup> startupClass, IServiceCollection iServiceCollection) throws Exception {
        IStartup startup = (IStartup) startupClass.getDeclaredConstructor().newInstance();
        //set IConfiguraiton object
           // startup.Conifiguration

            logger.info("register initial service pipeline");
            MveHelper.RegisterServices(iServiceCollection);
            MveHelper.Configure(iServiceCollection);
            //property configuration
           reader.LoadConfiguration(args);
            startup.configureServices(iServiceCollection);

           logger.info("configure pipeline");
           startup.configure(applicationBuilder);

    }


 public WebHostBuilder ConfigureHost(IAppConfigure appConfigure){

       appConfigure.configure(AppConfigurer.getInstance());

        return  this;
 }


    public  static WebHostBuilder build(String[] args, ClassLoader classLoader){

        return  new WebHostBuilder(args,classLoader);

    }

}
