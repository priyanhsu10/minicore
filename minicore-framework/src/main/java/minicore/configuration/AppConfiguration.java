package minicore.configuration;

import minicore.json.JsonHelper;
import minicore.mildlewares.exceptions.MvcException;

public class AppConfiguration implements IConfiguration {

    @Override
    public <T> T getValue(Class<T> t, String key) {
        String value = System.getProperty(key);
        if (value != null) {
            return JsonHelper.deserialize(value, t);

        }
        throw new MvcException("key:" + key + " not present ");
    }

    @Override
    public String getValue(String key) {
        return System.getProperty(key);
    }

}
