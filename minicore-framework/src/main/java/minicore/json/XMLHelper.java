package minicore.json;

    import com.fasterxml.jackson.core.JsonProcessingException;
    import com.fasterxml.jackson.databind.json.JsonMapper;
    import com.fasterxml.jackson.dataformat.xml.XmlMapper;

    import java.io.ByteArrayOutputStream;
    import java.util.List;

public class XMLHelper {
    public  static XmlMapper xmlMapper;
    static {
        xmlMapper = new XmlMapper();

    }
    public  static String serialize(Object object) {

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            return   xmlMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw  new RuntimeException(e);
        }
    }
    public  static <T> T deserialize(String data ,Class<T> aClass){

        // todo:see you latter this condition
        if( aClass.isAssignableFrom(List.class)){

       }
        try {
            return   xmlMapper.readValue(data,aClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }
    }
}
