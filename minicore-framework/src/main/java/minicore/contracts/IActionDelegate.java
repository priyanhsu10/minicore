package minicore.contracts;

public interface IActionDelegate {
    void invoke(HttpContext httpContext) throws Exception;
}
