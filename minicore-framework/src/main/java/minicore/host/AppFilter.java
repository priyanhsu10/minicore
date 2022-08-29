package minicore.host;

import minicore.contracts.HttpContext;

import javax.servlet.*;
import java.io.IOException;
import java.util.stream.Collectors;

public class AppFilter implements Filter {

    @Override

    public void init(FilterConfig filterConfig) {

        System.out.println("filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpContext httpContext = new HttpContext(servletRequest, servletResponse);
            WebHostBuilder.getAction().invoke(httpContext);
            WebHostBuilder.getServiceCollection().clearRequestObjects();

        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    private static void initialAction( HttpContext httpContext) {
        System.out.println("inital middleware");
    }
    @Override
    public void destroy() {

    }
}