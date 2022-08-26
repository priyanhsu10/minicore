package minicore.contracts.mvc;

import minicore.contracts.ioc.IServiceCollection;

@FunctionalInterface
public interface IConfigureMvc {
    void   configure(MvcConfigurer options);
}
