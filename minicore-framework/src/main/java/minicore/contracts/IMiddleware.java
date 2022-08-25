package minicore.contracts;


public interface IMiddleware {
     void next(IActionDelegate action , HttpContext httpContext) throws Exception;
}
