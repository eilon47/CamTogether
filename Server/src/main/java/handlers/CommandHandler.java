package handlers;

import database.DBClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xmls.*;
import converters.IConverter;
import converters.XmlConverter;

import javax.xml.bind.JAXBException;

public abstract class CommandHandler {
    protected static Logger logger = LogManager.getLogger("handlers");
    protected static DBClient dbClient = new DBClient();
    protected static XmlConverter converter = new XmlConverter();
    public abstract ResponseMessage handle(RequestMessage request);

    protected HeaderResponse createHeaderResponse(HeaderRequest headerRequest){
        HeaderResponse headerResponse = new HeaderResponse();
        headerResponse.setCommand(headerRequest.getCommand());
        headerResponse.setUsername(headerRequest.getUsername());
        headerResponse.setCommandSuccess(true);
        return headerResponse;
    }

    protected <T> T fromXmlToClass(String xml, Class<T> tClass){
        try {
            return converter.serializeFromString(xml, tClass);
        } catch (JAXBException ex){
            logger.warn("Failed creating class " + tClass.getName()+" from xml", ex);
            return null;
        }
    }

    protected <T> String fromClassToXml(T object){
        try {
            return converter.serializeToString(object);
        } catch (JAXBException ex){
            logger.warn("failed creating xml from class " + object.getClass().getName(), ex);
            return null;
        }
    }
}
