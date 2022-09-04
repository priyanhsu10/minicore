package minicore.configuration;

import java.util.Properties;

public class AppConfiguration implements IConfiguration{

   public static final Properties properties= new Properties();

    @Override
    public <T> T getValue(T t, String key) {
        return null;
    }
}
