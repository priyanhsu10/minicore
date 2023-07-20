package minicore.contracts.pipeline;

import minicore.contracts.IMiddleware;

import java.util.List;

public interface IApplicationBuilder {
    IApplicationBuilder use(Class<? extends IMiddleware> middlewareType);

    IApplicationBuilder useRouting();

    IApplicationBuilder useEndpoints();

    IApplicationBuilder useStaticFiles();

    IApplicationBuilder useAuthentication();

    List<Class<? extends IMiddleware>> getMidlewareTypes();
}
