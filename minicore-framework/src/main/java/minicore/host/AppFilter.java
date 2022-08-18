package minicore.host;

import minicore.contracts.HttpContext;

import javax.servlet.*;
import java.io.IOException;

public class AppFilter implements Filter {

    @Override

    public void init(FilterConfig filterConfig) {

        System.out.println("filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println(servletRequest);
        //trigger framework
        HttpContext actionContext = new HttpContext(servletRequest, servletResponse);
        try {
            WebHostBuilder.getAction() .next(actionContext);
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {

    }
}