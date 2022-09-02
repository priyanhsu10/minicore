package testclient.filters;

import minicore.contracts.HttpContext;
import minicore.contracts.filters.IResultExecutionFilter;
//By implementing  IResultExecutionFilter you can right your Custom Result filter
//use you can write your custom result filter and change the result before the response
public class TestResultFilter implements IResultExecutionFilter {
    @Override
    public void beforeResultExecute(HttpContext httpContext) {

        System.out.println("TestResultFilter:executed");
        //writing custom Header for
        httpContext.getResponse().addHeader("Framwork-Name","Minicore");
        httpContext.getResponse().addHeader("Developer","Priyanshu Parate");

    }
}
