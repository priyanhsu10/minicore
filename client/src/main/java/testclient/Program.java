package testclient;

import minicore.configuration.AppConfigurer;
import minicore.host.WebHostBuilder;

import java.util.HashMap;
import java.util.Map;

public class Program {
    public static void main(String[] args) {
       WebHostBuilder.build(args)
               //configure custom properties
               .ConfigureHost(option->{

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

