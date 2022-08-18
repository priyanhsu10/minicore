package minicore.testclient;

import minicore.contracts.HttpContext;
import minicore.contracts.IAction;
import minicore.contracts.IMiddleware;

public class Middleare2 implements IMiddleware {
    private IAction action;

    public Middleare2(IAction action) {
        this.action = action;
    }

    @Override
    public void invoke(HttpContext actionHttpContext) throws Exception {
        System.out.println("middleware 2 executing beofore");
        action.next(actionHttpContext);
        System.out.println("middleware 2 executing after");

    }
}
