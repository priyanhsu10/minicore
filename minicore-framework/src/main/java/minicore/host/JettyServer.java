package minicore.host;

import java.util.EnumSet;

import jakarta.servlet.DispatcherType;
import minicore.contracts.host.IServer;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class JettyServer implements IServer {
    private Server server;

    public void start() {

        QueuedThreadPool threadPool= new QueuedThreadPool();
        threadPool.setName("server");
        server = new Server(threadPool);
        ServletContextHandler context = new ServletContextHandler();
        context.addFilter(AppFilter.class,"/*",EnumSet.of(DispatcherType.REQUEST));
        server.setHandler(context);

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(Integer.parseInt(System.getProperty("appPort")));
        server.setConnectors(new Connector[] {connector});
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            connector.close();
        }
    }

    @Override
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
