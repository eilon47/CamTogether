package handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xmls.*;
import xsd.XsdUtils;

import javax.xml.bind.JAXBException;

public class MessageHandler {

    private CreateNewAlbumCommandHandler createNewAlbumCommandHandler = new CreateNewAlbumCommandHandler();
    protected static Logger logger = LogManager.getLogger("handlers");

    public String messageReceived(String xmlMessage) throws Exception  {
        logger.debug("message received " + xmlMessage);
        RequestMessage message = fromXmlToClass(xmlMessage, RequestMessage.class);
        logger.info("message received with command " + message.getHeader().getCommand().value());
        CommandsEnum cmd = message.getHeader().getCommand();
        ResponseMessage res = null;
        switch (cmd){
            case CREATE_NEW_ALBUM:
                res = createNewAlbumCommandHandler.handle(message);
                break;
            case ADD_NEW_PHOTO:
                break;
            case ADD_USER_TO_ALBUM:
                break;
            case GET_PHOTOS_FROM_ALBUM:
                break;
            default:
                break;
        }
        return fromClassToXml(res);
    }

    protected <T> T fromXmlToClass(String xml, Class<T> tClass){
        try {
            return XsdUtils.serializeFromXml(xml, tClass);
        } catch (JAXBException ex){
            logger.warn("Failed creating class from xml", ex);
            return null;
        }
    }

    protected <T> String fromClassToXml(T object){
        try {
            return XsdUtils.serializeToXML(object);
        } catch (JAXBException ex){
            logger.warn("failed creating xml from class", ex);
            return null;
        }
    }

    public static void main(String [] a){
        MessageHandler handler = new MessageHandler();

        RequestMessage requestMessage = new RequestMessage();
        HeaderRequest headerRequest = new HeaderRequest();
        headerRequest.setUserId("eilon");
        headerRequest.setCommand(CommandsEnum.CREATE_NEW_ALBUM);
        NewAlbumRequestBody body = new NewAlbumRequestBody();
        body.setAlbumName("my_first_album");
        body.setManager("eilon");
        String bodyString = handler.fromClassToXml(body);
        requestMessage.setBody(bodyString);
        requestMessage.setHeader(headerRequest);
        String xml = handler.fromClassToXml(requestMessage);
        try {
            handler.messageReceived(xml);
        } catch (Exception e){
            logger.warn(e);
        }
    }

}
