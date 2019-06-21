package handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xmls.*;
import converters.XmlConverter;
import javax.xml.bind.JAXBException;

public class MessageHandler {
    private XmlConverter converter = new XmlConverter();
    protected static Logger logger = LogManager.getLogger("handlers");

    public String messageReceived(String xmlMessage){
        logger.debug("message received " + xmlMessage);
        RequestMessage message = fromXmlToClass(xmlMessage, RequestMessage.class);
        logger.info("message received with command " + message.getHeader().getCommand().value());
        CommandsEnum cmd = message.getHeader().getCommand();
        ResponseMessage res;
        try {
            CommandHandler handler = HandlersFactory.getHandler(cmd);
            res = handler.handle(message);
            String ret = fromClassToXml(res);
            logger.debug("Response = [" + ret + "]");
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Got invalid command: " + cmd.toString());
            return "Invalid Command";
        }
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
