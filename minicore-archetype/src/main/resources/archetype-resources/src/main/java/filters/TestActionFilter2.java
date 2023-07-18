#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.filters;

import minicore.contracts.HttpContext;
import minicore.contracts.filters.IActionFilter;

public class TestActionFilter2 implements IActionFilter {

    //execute method before action call
    @Override
    public void beforeExecute(HttpContext httpContext) {
        //contain the action parameters that you can change
        // httpContext.ActionContext.MethodParameters
        System.out.println("TestActionFilter2 :beforeExecute");
        //for short-circuiting pipe set action result value
        //httpContext.ActionContext.ActionResult
        // example httpContext.ActionContext.ActionResult= new NotFoundResult();



    }

    @Override
    public void afterExecute(HttpContext httpContext) {
        //perform any logic after action execution
        // here Action result will be present
        System.out.println("TestActionFilter2 :afterExecute");
    }
}

