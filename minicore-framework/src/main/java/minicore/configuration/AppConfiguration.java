package minicore.configuration;

import minicore.json.JsonHelper;

import java.util.Properties;

public class AppConfiguration implements IConfiguration{


    @Override
    public <T> T getValue(Class<T> t, String key) {
        String value= System.getProperty(key);
        if(value!=null){
            return JsonHelper.deserialize(value,t);

        }
        throw new RuntimeException("key:"  +key+" not present ");
    }
    private Object getObject(String value, Class<?> parameterType) {

        return parameterType.cast(value);
    }

    @Override
    public String getValue(String key) {
        return System.getProperty(key);
    }

}
