package minicore.contracts.mvc;

@FunctionalInterface
public interface IConfigureMvc {
    void configure(MvcConfigurer options);
}
