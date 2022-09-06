package testclient;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import minicore.configuration.AppConfigurer;
import minicore.host.WebHostBuilder;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.Map;

public class Program {
    public static void main(String[] args) {
        //set your logging level
        //framework not giving you the in build implementation but provide sla4j dependency so you can choose
        //your own login provider
        //for this example we use logback
        //add logback.xml configuration and remove below lines
        Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO);

        WebHostBuilder.build(args)
                //configure custom properties
                .ConfigureHost(option -> {

//                       option.addPropertyFile("CustomFile.properties");
//
//                       option.addJsonFile("CustomjsonFile.json");
//
//                       option.addXmlFile("CustomxmlFile.xml");
                })

                .useStartup(AppStartup.class)

                .run();
    }
}

