package minicore.host;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServlet;
import minicore.contracts.HttpContext;
import org.eclipse.jetty.server.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.stream.Collectors;

public class AppFilter extends HttpServlet implements Filter {
    public  static Logger logger= LoggerFactory.getLogger(AppFilter.class);

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
            logger.error(e.getMessage(),e);
        }
    }

    private static void initialAction( HttpContext httpContext) {
        System.out.println("inital middleware");
    }
    @Override
    public void destroy() {

    }
}