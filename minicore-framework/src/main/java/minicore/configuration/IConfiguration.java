package minicore.configuration;

public interface IConfiguration {
    <T> T getValue(T t,String key);

}
