package minicore.configuration;

import minicore.contracts.ioc.IServiceCollection;

import java.util.Map;

public class JsonXMLConfigParser implements ICustomConfigurer {

    private final String path;

    public JsonXMLConfigParser(String path) {
        this.path = path;
    }

    @Override
    public Map<String, Object> custom(IServiceCollection service) {
        return null;
    }
}
