package minicore.configuration;

public interface IConfiguration {
    <T> T getValue(Class<T> type,String key);
    String getValue(String key);

}
