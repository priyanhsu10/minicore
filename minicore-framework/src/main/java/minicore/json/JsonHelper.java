package minicore.json;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class JsonHelper {
    public  static JsonMapper jsonMapper ;
    static {
        jsonMapper = new JsonMapper();

    }
    public  static String serialize(Object object) {

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            return   jsonMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw  new RuntimeException(e);
        }
    }
    public  static <T> T deserialize(String data ,Class<T> aClass){

        // todo:see you latter this condition
        if( aClass.isAssignableFrom(List.class)){

        }
        try {
            return   jsonMapper.readValue(data,aClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }
    }
}
