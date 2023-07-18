package io.testing.middlewares;

import minicore.contracts.HttpContext;
import minicore.contracts.IActionDelegate;
import minicore.contracts.IMiddleware;

import java.util.UUID;

//In this way your can  write your custom middleware
public class TransactionIdMiddleware implements IMiddleware {
    private  IActionDelegate action;
    @Override
    public void setNext(IActionDelegate action) {
        this.action= action;
    }

    @Override
    public void next(HttpContext httpContext) throws Exception {
        //adding the transaction id at the start of pipeline so you can access
        // So you can maintain all Database transactions agains  the id i
        httpContext.getData().put("trazId", UUID.randomUUID().toString());

        //perform  your custom logic before executing next middleware
        System.out.println("TransactionIdMiddleware : custom logic before executing next middleware ");
        action.invoke(httpContext);
        System.out.println("TransactionIdMiddleware : custom logic after executing next middleware ");
        //perform  your custom logic After executing next middleware
    }
}
