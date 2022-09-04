package minicore;

import com.fasterxml.jackson.core.JsonProcessingException;
import minicore.configuration.SystemConfig;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {


        SystemConfig c= SystemConfig.load();
        System.out.println(c.getPort());
    }
}
