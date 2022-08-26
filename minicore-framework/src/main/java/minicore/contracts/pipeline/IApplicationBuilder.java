package minicore.contracts.pipeline;

import minicore.contracts.IAction;
import minicore.contracts.IActionDelegate;
import minicore.contracts.IMiddleware;

public interface IApplicationBuilder  {
    IActionDelegate build();
    IApplicationBuilder  use(Class<? extends IMiddleware> middlewareType);
    IApplicationBuilder map(String url,IAction action);
}
