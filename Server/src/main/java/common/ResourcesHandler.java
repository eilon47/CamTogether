package common;

import java.net.URL;

public class ResourcesHandler {
    public static String communication_info = "communication.properties";
    public static String init_tables = "init_tables.json";

    public static String getResourceFilePath(String fileName){
        //Get file from resources folder
        ClassLoader classLoader = ResourcesHandler.class.getClassLoader();
        URL url = classLoader.getResource(fileName);
        return url.getFile();
    }
}
