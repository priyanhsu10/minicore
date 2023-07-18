package ${package};
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import minicore.host.WebHostBuilder;
import org.slf4j.LoggerFactory;

public class Program {
    public static void main(String[] args) {
        //set your logging level
        //framework not giving you the in build implementation but provide sla4j dependency so you can choose
        //your own login provider
        //for this example we use logback
        //add logback.xml configuration and remove below lines
        Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO);

        WebHostBuilder.build(args, new Program().getClass().getClassLoader())
                //configure custom properties
                .ConfigureHost(option -> {

                    option.addPropertyFile("custom.properties");
                    option.addXmlFile("propertieXML.xml");
                })
                .useStartup(AppStartup.class)

                .run();
    }
}

