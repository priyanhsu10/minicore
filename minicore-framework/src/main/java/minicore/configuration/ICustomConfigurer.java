package minicore.configuration;

import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.mvc.MvcConfigurer;

import java.io.IOException;
import java.util.Map;

public interface ICustomConfigurer {
    Map<String , Object>  custom() throws IOException;
}
