package io.testing.filters;

import minicore.contracts.HttpContext;
import minicore.contracts.filters.IResultExecutionFilter;

//By implementing  IResultExecutionFilter you can right your Custom Result filter
//use you can write your custom result filter and change the result before the response
public class CustomResultFilter implements IResultExecutionFilter {
    @Override
    public void beforeResultExecute(HttpContext httpContext) {

        //writing custom Header for
        System.out.println("CustomResultFilter executed");
        httpContext.getResponse().addHeader("executed-action",httpContext.getEndPointMetadata().DisplayName);
    }
}
