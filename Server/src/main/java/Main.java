import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Main {
    public static Logger logger = LogManager.getLogger();
    public static void main(String args[]){
        logger.info("Here we go...");
        logger.debug("should not be written");
        logger.warn("is written");
    }
}
