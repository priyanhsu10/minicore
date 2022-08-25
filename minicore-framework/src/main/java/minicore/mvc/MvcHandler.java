package minicore.mvc;

import minicore.contracts.HttpContext;
import minicore.contracts.filters.IActionFilter;
import minicore.contracts.filters.IAuthenticationFilter;
import minicore.contracts.filters.IExceptionFilter;

import java.util.ArrayList;
import java.util.List;

public class MvcHandler {

    private HttpContext Context;
    private List<Class<? extends IAuthenticationFilter>> AuthFilters = new ArrayList<>();
    private List<Class<? extends IExceptionFilter>> exceptionFilters = new ArrayList<>();
    private List<Class<? extends IActionFilter>> actionFilters = new ArrayList<>();

    public MvcHandler(HttpContext context) {
        Context = context;

    }

    void process() {
        //1 . execute  authfilter
        //2. controller instanciation
        //3.model binding
        //try{
        //4.execute before action global filter
        //5.execute controller before action filter
        //6. execute before action filter
        //7. execute action ()
        // 6. execute after action filter
        //5.execute controller after action filter
        // 4.execute after action global filter
        //
        // }
        //catch(Exception e){

        //execute exception filters
//}


    }

}
