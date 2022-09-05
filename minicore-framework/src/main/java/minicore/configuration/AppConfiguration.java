package minicore.configuration;

import java.util.Properties;

public class AppConfiguration implements IConfiguration{


    @Override
    public <T> T getValue(Class<T> t, String key) {
        return (T) t.cast( System.getProperty(key));
    }

    @Override
    public String getValue(String key) {
        return System.getProperty(key);
    }

}
