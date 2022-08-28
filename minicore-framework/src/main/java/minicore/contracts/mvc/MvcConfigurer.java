package minicore.contracts.mvc;

import minicore.contracts.filters.IActionFilter;
import minicore.contracts.filters.IAuthenticationFilter;
import minicore.contracts.filters.IExceptionFilter;
import minicore.contracts.filters.IResultExecutionFilter;
import minicore.contracts.formaters.IInputFormatter;
import minicore.contracts.formaters.IOutputFormatter;
import minicore.contracts.ioc.IServiceCollection;
import minicore.ioc.container.Scope;

import java.util.ArrayList;
import java.util.List;

public class MvcConfigurer {


    public List<Class<? extends IActionFilter>> getGlobalFilters() {
        return globalFilters;
    }

    public List<Class<? extends IOutputFormatter>> getOutputFormatters() {
        return outputFormatters;
    }
    public List<Class<? extends IExceptionFilter>> getExecptionFilter() {
        return exceptionFilters;
    }
    public List<Class<? extends IResultExecutionFilter>> getResultExecutionFilter() {
        return resultExecutionFilter;
    }


    public List<Class<? extends IInputFormatter>> getInputFormatters() {
        return inputFormatters;
    }

    private final  List<Class<? extends IActionFilter>> globalFilters = new ArrayList<>();
    private final  List<Class<? extends IExceptionFilter>> exceptionFilters = new ArrayList<>();
    private final  List<Class<? extends IResultExecutionFilter>> resultExecutionFilter = new ArrayList<>();
    public List<Class<? extends IAuthenticationFilter>> getAuthFilters() {
        return authFilters;
    }

    private final  List<Class<? extends IAuthenticationFilter>> authFilters = new ArrayList<>();
    private final  List<Class<? extends IOutputFormatter>> outputFormatters = new ArrayList<>();
    private final  List<Class<? extends IInputFormatter>> inputFormatters = new ArrayList<>();
    private final IServiceCollection serviceCollection;

    public MvcConfigurer(IServiceCollection serviceCollection) {
        this.serviceCollection = serviceCollection;
    }

    public MvcConfigurer addGlobalFilter(Class<? extends IActionFilter> actionFilter){
        serviceCollection.register(actionFilter, Scope.Singleton);
        globalFilters.add(actionFilter);
        return this;
    }
    public MvcConfigurer addExceptionFilter(Class<? extends IExceptionFilter> actionFilter){
        serviceCollection.register(actionFilter, Scope.Singleton);
        exceptionFilters.add(actionFilter);
        return this;
    }
    public MvcConfigurer addResultFilter(Class<? extends IResultExecutionFilter> actionFilter){
        serviceCollection.register(actionFilter, Scope.Singleton);
        resultExecutionFilter.add(actionFilter);
        return this;
    }
    public MvcConfigurer addAuthFilter(Class<? extends IAuthenticationFilter> actionFilter){
        serviceCollection.register(actionFilter, Scope.Singleton);
        authFilters.add(actionFilter);
        return this;
    }
    public MvcConfigurer addOutputFormatter(Class<?  extends IOutputFormatter> actionFilter){
        serviceCollection.register(actionFilter, Scope.Singleton);
        outputFormatters.add(actionFilter);
        return this;
    }
    public MvcConfigurer addInputFormatter(Class<?  extends IInputFormatter> actionFilter){
        serviceCollection.register(actionFilter, Scope.Singleton);
        inputFormatters.add(actionFilter);
        return this;
    }

    public  static  void configureMvc(IServiceCollection services, IConfigureMvc config){
        MvcConfigurer configurer= services.resolve(MvcConfigurer.class);
        config.configure(configurer);
}
}


