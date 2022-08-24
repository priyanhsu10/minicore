package minicore.contracts;
public interface IAction {
   void next(IActionDelegate next, HttpContext httpContext) throws Exception ;

}
