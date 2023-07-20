package minicore.host;

import minicore.contracts.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

public class AppFilter implements Filter {
    public static Logger logger = LoggerFactory.getLogger(AppFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {

        System.out.println("filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            HttpContext httpContext = new HttpContext(servletRequest, servletResponse);
            WebHostBuilder.getAction().invoke(httpContext);
            WebHostBuilder.getServiceCollection().clearRequestObjects();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {

    }
}