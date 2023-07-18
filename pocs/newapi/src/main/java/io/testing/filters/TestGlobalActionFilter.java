package io.testing.filters;

import minicore.contracts.HttpContext;
import minicore.contracts.filters.IActionFilter;

public class TestGlobalActionFilter implements IActionFilter {
    //execute method before action call
    @Override
    public void beforeExecute(HttpContext httpContext) {
        //contain the action parameters that you can change
        // httpContext.ActionContext.MethodParameters
        System.out.println("TestGlobalActionFilter :beforeExecute");
        //for short-circuiting pipe set action result value
        //httpContext.ActionContext.ActionResult
        // example httpContext.ActionContext.ActionResult= new NotFoundResult();



    }

    @Override
    public void afterExecute(HttpContext httpContext) {
        //perform any logic after action execution
        // here Action result will be present
        System.out.println("TestGlobalActionFilter :afterExecute");
    }
}
