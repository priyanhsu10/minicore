package minicore.contracts;


public interface IMiddleware {
     void invoke(HttpContext httpContext) throws Exception;
}
