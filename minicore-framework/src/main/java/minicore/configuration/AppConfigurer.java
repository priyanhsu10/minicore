package minicore.configuration;

import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.mvc.MvcConfigurer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AppConfigurer {
    private final static AppConfigurer instance;

    static {
        instance = new AppConfigurer();
    }

    public static AppConfigurer getInstance() {
        return instance;
    }

    public Map<Object, Object> getCustomProperties() {
        return customProperties;
    }

    private final Map<Object, Object> customProperties;

    public AppConfigurer() {
        this.customProperties = new HashMap<>();
    }

    public void addJsonFile(String filePath) {

        // read and put in custom properties
    }

    public void addXmlFile(String filePath) {
        // read and put in custom properties
    }

    public void custom(IServiceCollection serviceCollection, ICustomConfigurer custom) {
        Map<String, Object> customObject = custom.custom(serviceCollection);
        this.customProperties.putAll(customObject);
    }

    public void addPropertyFile(String filePath) {

        this.customProperties.putAll(readConfig(filePath));
    }

    public static void Configure(IAppConfigure appConfigure) {

        appConfigure.configure(AppConfigurer.getInstance());

    }

    private Map<Object, Object> readConfig(String filename) {
        Properties p = new Properties();
        try (FileInputStream f = (FileInputStream) ConfigurationReader.class.getClassLoader().getResourceAsStream(SystemConfig.class.getClassLoader().getResource(filename).getPath())) {

            p.load(f);

        } catch (FileNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return p;

    }
}
