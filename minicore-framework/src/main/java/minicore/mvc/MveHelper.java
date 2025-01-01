package minicore.mvc;

import minicore.configuration.AppConfiguration;
import minicore.configuration.AppConfigurer;
import minicore.configuration.IConfiguration;
import minicore.contracts.HttpContext;
import minicore.contracts.filters.IFilterProvider;
import minicore.contracts.formaters.IFormatProvider;
import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.modelbinding.DefaultModelBinder;
import minicore.contracts.modelbinding.IModelBinder;
import minicore.contracts.mvc.IMvcHandler;
import minicore.contracts.mvc.MvcConfigurer;
import minicore.contracts.results.IResultExectutor;
import minicore.contracts.results.ResultExecutor;
import minicore.mvc.filters.MvcExceptionFilter;
import minicore.mvc.formatters.input.DefaultInputFormatter;
import minicore.mvc.formatters.FormatProvider;
import minicore.mvc.formatters.input.JsonInputFormatter;
import minicore.mvc.formatters.input.XmlInputFormatter;
import minicore.mvc.formatters.output.DefaultOutputFormatter;
import minicore.mvc.formatters.output.JsonOutputFormatter;
import minicore.mvc.formatters.output.XmlOutputFormatter;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

public  class MveHelper {
    public  static  void RegisterServices(IServiceCollection serviceCollection){
        registerInitialServices(serviceCollection);
        registerMvcServices(serviceCollection);
    }
    private static void registerInitialServices(IServiceCollection iServiceCollection) {
        iServiceCollection.addSingleton(IServiceCollection.class,()->iServiceCollection);
        registerMvcServices(iServiceCollection);
        iServiceCollection.addSingleton(ILoggerFactory.class, ()-> LoggerFactory.getILoggerFactory());
//        iServiceCollection.addTransient(Logger.class, ()-> LoggerFactory.getILoggerFactory().getLogger());
        //wherever resolver needed in the pipeline it is present
        iServiceCollection.addSingleton(IConfiguration.class, AppConfiguration.class);
        HttpContext.services= iServiceCollection;
    }

    private static void registerMvcServices(IServiceCollection iServiceCollection) {
        iServiceCollection.addSingleton(AppConfigurer.class,AppConfigurer.class);
        iServiceCollection.addSingleton(MvcConfigurer.class,MvcConfigurer.class);
        iServiceCollection.addSingleton(IModelBinder.class, DefaultModelBinder.class);
        iServiceCollection.addSingleton(IFilterProvider.class, FilterProvider.class);
        iServiceCollection.addSingleton(IResultExectutor.class, ResultExecutor.class);
        iServiceCollection.addSingleton(IFormatProvider.class, FormatProvider.class);
        iServiceCollection.addTransient(IMvcHandler.class, MvcHandler.class);

    }

    public static void Configure(IServiceCollection services) {

        MvcConfigurer.configureMvc(services,options->{
            //input formatter
            options.addInputFormatter(DefaultInputFormatter.class);
            options.addInputFormatter(JsonInputFormatter.class);
            options.addInputFormatter(XmlInputFormatter.class);
            //output formatter
            options.addOutputFormatter(DefaultOutputFormatter.class);
            options.addOutputFormatter(JsonOutputFormatter.class);
            options.addOutputFormatter(XmlOutputFormatter.class);

            //result Filters
            options.addResultFilter(DefaultResultFilter.class);

            //exception Filter
            options.addExceptionFilter(MvcExceptionFilter.class);
            //


        });
    }
}
