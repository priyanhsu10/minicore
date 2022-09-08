package minicore.configuration;

import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.mvc.MvcConfigurer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
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

    public void addJsonFile(String filePath) throws IOException {
        URL resource = SystemConfig.class.getClassLoader().getResource(filePath);

        ICustomConfigurer jsonConfigurer=  new JsonConfigParser(resource.getPath());
        this.customProperties.putAll(jsonConfigurer.custom());
        // read and put in custom properties
    }

    public void addXmlFile(String filePath) throws IOException {
        if(!filePath.endsWith("xml")){
            throw new InvalidPropertiesFormatException(filePath);
        }
        URL resource = SystemConfig.class.getClassLoader().getResource(filePath);
        this.customProperties.putAll(readConfigFromXml(resource.getPath()));
    }

    public void custom(ICustomConfigurer custom) throws IOException {
        Map<String, Object> customObject = custom.custom();
        this.customProperties.putAll(customObject);
    }

    public void addPropertyFile(String filePath) {
        URL resource = SystemConfig.class.getClassLoader().getResource(filePath);
        this.customProperties.putAll(readConfig(resource.getPath()));
    }

    public static void Configure(IAppConfigure appConfigure) {

        appConfigure.configure(AppConfigurer.getInstance());

    }

    private Map<Object, Object> readConfig(String filename) {
        Properties p = new Properties();
        try (FileInputStream f = (FileInputStream) ConfigurationReader.class.getClassLoader().getResourceAsStream(filename)) {

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
    private Map<Object, Object> readConfigFromXml(String filename) {
        Properties p = new Properties();
        try (FileInputStream f = (FileInputStream) ConfigurationReader.class.getClassLoader().getResourceAsStream(filename)) {

            p.loadFromXML(f);

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
