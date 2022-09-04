package minicore.configuration;

import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.mvc.MvcConfigurer;

import java.util.Map;

public interface ICustomConfigurer {
    Map<String , Object>  custom(IServiceCollection service);
}
