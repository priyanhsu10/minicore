package minicore.configuration;

import minicore.contracts.ioc.IServiceCollection;

import java.util.Map;

public class PropertyConfigParser  implements ICustomConfigurer {

    private final String path;

    public PropertyConfigParser(String path) {
        this.path = path;
    }

    @Override
    public Map<String, Object> custom() {

        return null;
    }
}
