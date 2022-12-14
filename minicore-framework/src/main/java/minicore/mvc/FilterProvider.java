package minicore.mvc;

import minicore.contracts.HttpContext;
import minicore.contracts.filters.*;
import minicore.contracts.mvc.MvcConfigurer;

import java.util.ArrayList;
import java.util.List;

public class FilterProvider implements IFilterProvider {

    private final MvcConfigurer configurer;
    private List<IActionFilter> globalFilter = new ArrayList<>();
    private List<IAuthenticationFilter> authFilters = new ArrayList<>();
    private List<IExceptionFilter> exceptionFilters = new ArrayList<>();
    private List<IResultExecutionFilter> resultExecutionFilters = new ArrayList<>();

    public FilterProvider(MvcConfigurer configurer) {

        this.configurer = configurer;
        preparefilters();
    }

    @Override
    public List<IActionFilter> getGlobalFilters() {
        return globalFilter;
    }

    @Override
    public List<IAuthenticationFilter> getAuthFilters() {
        return authFilters;
    }

    @Override
    public List<IExceptionFilter> getExceptionFilters() {
        return exceptionFilters;
    }

    @Override
    public List<IResultExecutionFilter> getResultExecutionFilters() {
        return resultExecutionFilters;
    }

    private void preparefilters() {
        //global filter
        prepareGlobalFilter();
        //configure auth filter
        prepareAuthFilters();
        //configure action filter

        //configure exception Filters
        prepareExceptionFilters();

        // configure Result filters
        prepareResultFilter();

    }

    private void prepareGlobalFilter() {
        configurer.getGlobalFilters().forEach(x -> {
            IActionFilter f = HttpContext.services.resolve(x);
            this.globalFilter.add(f);
        });
    }
    private void prepareResultFilter() {
        configurer.getResultExecutionFilter().forEach(x -> {
            IResultExecutionFilter f = HttpContext.services.resolve(x);
            this.resultExecutionFilters.add(f);
        });
    }
    private void prepareAuthFilters() {

        configurer.getAuthFilters().forEach(x -> {
            IAuthenticationFilter f = HttpContext.services.resolve(x);
            this.authFilters.add(f);
        });
    }


    private void prepareExceptionFilters() {

        configurer.getExecptionFilter().forEach(x -> {
            IExceptionFilter f = HttpContext.services.resolve(x);
            this.exceptionFilters.add(f);
        });

    }
}
