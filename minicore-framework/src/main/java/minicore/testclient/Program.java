package minicore.testclient;

import minicore.contracts.HttpContext;
import minicore.host.WebHostBuilder;

public class Program {
    public static void main(String[] args) {
       WebHostBuilder.build(args)
               .useStartup(AppStartup.class)
               .run();
    }
}

