package common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigHolder {
    private static Properties comm = new Properties();
    private static Logger logger = LogManager.getLogger();
    static  {
        try {
            comm.load(new FileInputStream("communication.properties"));
        }catch (IOException ex){
            logger.error("Could not read communication.properties file", ex);
        }
    }
    public static String getCommunicationProp(String key, String defVal){
        return comm.getProperty(key, defVal);
    }

    public static int getCommunicationIntProp(String key, int defVal){
        return Integer.getInteger(comm.getProperty(key, Integer.toString(defVal)));
    }


}
