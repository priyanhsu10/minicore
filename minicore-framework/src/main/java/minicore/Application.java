package minicore;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import minicore.configuration.SystemConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class Application {
    public static void main(String[] args) throws IOException {


//        JavaPropsMapper mapper = new JavaPropsMapper();
//        URL resource = SystemConfig.class.getClassLoader().getResource("app.devlopment.properties.json");
//        // convert JSON file to map
//        ObjectMapper objectMapper = new ObjectMapper();
//        byte[] mapData = Files.readAllBytes(Paths.get(resource.getPath()));
//        Map<String, String> myMap = objectMapper.readValue(mapData, HashMap.class);
//        System.out.println("Map is: "+myMap);
//        // print map entries
//
//
//        Properties p= mapper.writeValueAsProperties(myMap);
//        for(Map.Entry<Object,Object> s:p.entrySet()){
//            System.out.println(s.getKey()+"="+s.getValue());
//
//        }

    }
}
class TestModel{
    String name;
    int port;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }

    boolean password;



}