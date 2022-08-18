package minicore.testclient;

import minicore.contracts.HttpContext;
import minicore.contracts.IAction;
import minicore.contracts.IMiddleware;

public class Middleare1 implements IMiddleware {
    private IAction action;

    public Middleare1(IAction action) {


        this.action = action;
    }

    @Override
    public void invoke(HttpContext actionHttpContext) throws Exception {
        System.out.println("middleware 1 executing beofore");
        action.next(actionHttpContext);
        System.out.println("middleware 1 executing after");

    }
}
