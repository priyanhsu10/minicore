package minicore.configuration;


import minicore.json.JsonHelper;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SystemConfig {
    private   int port;
    private   String profile;
    private String application;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public SystemConfig() {
    }

     public static SystemConfig load ()  {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("startup.json");

         String systemConfigString = null;
         try {
             systemConfigString = Files.readAllLines(Paths.get(resource.getPath())).stream().collect(Collectors.joining());
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
         return JsonHelper.deserialize(systemConfigString,SystemConfig.class);

    }
}
