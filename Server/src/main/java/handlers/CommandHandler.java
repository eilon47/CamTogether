package handlers;

import database.DBClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xmls.*;
import xsd.XsdUtils;

import javax.xml.bind.JAXBException;

public abstract class CommandHandler {
    protected static Logger logger = LogManager.getLogger("handlers");
    protected static DBClient dbClient = new DBClient();

    public abstract ResponseMessage handle(RequestMessage request);

    protected HeaderResponse createHeaderResponse(HeaderRequest headerRequest){
        HeaderResponse headerResponse = new HeaderResponse();
        headerResponse.setCommand(headerRequest.getCommand());
        headerResponse.setUserId(headerRequest.getUserId());
        headerResponse.setCommandSuccess(true);
        return headerResponse;
    }

    protected <T> T fromXmlToClass(String xml, Class<T> tClass){
        try {
            return XsdUtils.serializeFromXml(xml, tClass);
        } catch (JAXBException ex){
            logger.warn("Failed creating class " + tClass.getName()+" from xml", ex);
            return null;
        }
    }

    protected <T> String fromClassToXml(T object){
        try {
            return XsdUtils.serializeToXML(object);
        } catch (JAXBException ex){
            logger.warn("failed creating xml from class " + object.getClass().getName(), ex);
            return null;
        }
    }
}
