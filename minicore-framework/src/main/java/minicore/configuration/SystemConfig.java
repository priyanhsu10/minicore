package minicore.configuration;


import minicore.json.JsonHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

     public static SystemConfig load (ClassLoader classLoader)  {

         try {
             String systemConfigString = getContent("startup.json",classLoader);
             return JsonHelper.deserialize(systemConfigString,SystemConfig.class);

         } catch (IOException e) {
             //todo:fix exception
             throw new RuntimeException(e);
         }

    }

    public static String getContent(String fileName, ClassLoader classLoader) throws IOException {
        //Creating instance to avoid static member methods
        InputStream is = getFileAsIOStream(fileName,classLoader);
       return   getFileContent(is);

    }
    private  static InputStream getFileAsIOStream(final String fileName,ClassLoader classLoader)
    {
        InputStream ioStream = classLoader
                .getResourceAsStream(fileName);

        if (ioStream == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }
        return ioStream;
    }
    private static String getFileContent(InputStream is) throws IOException
    {
        StringBuilder sb= new StringBuilder();
        try (InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr);)
        {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            is.close();
        }
        return sb.toString();
    }
}
