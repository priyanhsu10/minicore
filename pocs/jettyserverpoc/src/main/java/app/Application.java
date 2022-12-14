package app;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class Application {
    public static void main(String[] args) {
        JettyServer server= new JettyServer();

       server.start();
    }
}

 class JettyServer {
    private Server server;

    public void start() {

        server = new Server();
        ServletHandler servletHandler= new ServletHandler();
        servletHandler.addFilterWithMapping(AppFilter.class, "/*",
                EnumSet.of(DispatcherType.REQUEST));
        servletHandler.addServletWithMapping(BlockingServlet.class,"/status");
        server.setHandler(servletHandler);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8090);
        server.setConnectors(new Connector[] {connector});
        try {
            server.start();
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
