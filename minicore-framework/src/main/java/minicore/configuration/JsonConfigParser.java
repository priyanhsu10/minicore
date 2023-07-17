package minicore.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JsonConfigParser implements ICustomConfigurer{

    private final String path;

    public JsonConfigParser(String  path) {
        this.path = path;
    }

    @Override
    public Map<String, Object> custom() throws IOException {
        JavaPropsMapper mapper = new JavaPropsMapper();

        // convert JSON file to map
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] mapData = Files.readAllBytes(Paths.get(path));
        HashMap<String, String> myMap = new HashMap<String, String>();
        myMap = objectMapper.readValue(mapData, HashMap.class);
        // print map entries
        Properties p= mapper.writeValueAsProperties(myMap);
        Map<String,Object> result= new HashMap<>();
        p.forEach((key, value) -> {
            result.put(key.toString(), value);
            System.out.println(key + "=" + value);

        });
        return result;
    }
}
