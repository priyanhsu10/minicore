package minicore.contracts;
@FunctionalInterface
public interface IAction {
  void next(HttpContext httpContext) throws Exception;
}
