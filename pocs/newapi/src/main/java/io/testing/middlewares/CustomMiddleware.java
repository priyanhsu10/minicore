package io.testing.middlewares;

import minicore.contracts.HttpContext;
import minicore.contracts.IActionDelegate;
import minicore.contracts.IMiddleware;
//In this way your can  write your custom middleware
public class CustomMiddleware implements IMiddleware {
    private  IActionDelegate action;
    @Override
    public void setNext(IActionDelegate action) {
        this.action= action;
    }

    @Override
    public void next(HttpContext httpContext) throws Exception {

        //perform  your custom logic before executing next middleware
        System.out.println("CustomMiddleware : custom logic before executing next middleware ");
        action.invoke(httpContext);
        System.out.println("CustomMiddleware : custom logic after executing next middleware ");
        //perform  your custom logic After executing next middleware
    }
}
