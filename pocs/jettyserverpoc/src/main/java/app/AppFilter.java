package app;

import javax.servlet.*;
import java.io.IOException;

public class AppFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.getWriter().println("<h1>testing 123</h1>");
    }

    @Override
    public void destroy() {

    }
}
