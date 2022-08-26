package minicore.contracts;


public interface IMiddleware {
      void setNext(IActionDelegate action);

     void next(HttpContext httpContext) throws Exception;
}
