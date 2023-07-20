package minicore.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

        ICustomConfigurer jsonConfigurer = new JsonConfigParser(resource.getPath());
        this.customProperties.putAll(jsonConfigurer.custom());
        // read and put in custom properties
    }

    public void addXmlFile(String filePath) {
        if (!filePath.endsWith("xml")) {
            throw new RuntimeException(new InvalidPropertiesFormatException(filePath));
        }
        Map<Object, Object> objectObjectMap = readXmlConfig(filePath);
        this.customProperties.putAll(objectObjectMap);
    }

    public void custom(ICustomConfigurer custom) {
        Map<String, Object> customObject = getCustomProperties(custom);
        if (customObject == null || customObject.isEmpty()) {
            return;
        }
        this.customProperties.putAll(customObject);
    }

    private static Map<String, Object> getCustomProperties(ICustomConfigurer custom) {
        Map<String, Object> customObject = null;
        try {
            customObject = custom.custom();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customObject;
    }

    public void addPropertyFile(String filePath) {
        Map<Object, Object> objectObjectMap = readConfig(filePath);
        this.customProperties.putAll(objectObjectMap);
    }

    public static void Configure(IAppConfigure appConfigure) {

        appConfigure.configure(AppConfigurer.getInstance());

    }

    private Map<Object, Object> readConfig(String filename) {
        Properties p = new Properties();
        try (InputStream f = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {

            p.load(f);

        } catch (FileNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return p;

    }

    private Map<Object, Object> readXmlConfig(String filename) {
        Properties p = new Properties();
        try (InputStream f = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {

            p.loadFromXML(f);

        } catch (FileNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return p;

    }

    private Map<Object, Object> readConfigFromXml(String filename) {
        Properties p = new Properties();
        try (FileInputStream f = (FileInputStream) ConfigurationReader.class.getClassLoader()
                .getResourceAsStream(filename)) {

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
