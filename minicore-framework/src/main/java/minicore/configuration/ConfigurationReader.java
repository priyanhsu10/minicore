package minicore.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;

public class ConfigurationReader {
    private  ClassLoader classLoader;
    public ConfigurationReader(ClassLoader classLoader) {
        this.classLoader=classLoader;
        this.config = SystemConfig.load(classLoader);
    }

    private SystemConfig config;
    public Properties properties = new Properties();

    public void LoadConfiguration(String[] args) {
        properties.put("appName",config.getApplication());
        properties.put("appPort",String.valueOf(config.getPort()));
        properties.put("profile",config.getProfile());

        String fileName = "app.properties";

        readConfig(fileName);
        //read app.properties.json
        // read app.<profile>.properties.json
        String profileProperties = "app." + config.getProfile() + ".properties";
        readConfig(profileProperties);

        // custom configuration extension
        properties.putAll(AppConfigurer.getInstance().getCustomProperties());
        // read enviorment variables
        properties.putAll(System.getenv());

        //command line variables

        //setting properties
      System.getProperties().putAll(properties);
    }

    private void readConfig(String filename) {

        try ( InputStream ioStream = classLoader
                .getResourceAsStream(filename);) {
            if(ioStream==null){

                return;
            }
            properties.load(ioStream);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }


    }


}
