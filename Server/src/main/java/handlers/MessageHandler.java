package handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xmls.*;
import xsd.XsdUtils;

import javax.xml.bind.JAXBException;

public class MessageHandler {

    private CreateNewAlbumCommandHandler createNewAlbumCommandHandler = new CreateNewAlbumCommandHandler();
    private NewPhotoCommandHandler newPhotoCommandHandler = new NewPhotoCommandHandler();
    private AddUserToAlbumHandler addUserToAlbumHandler = new AddUserToAlbumHandler();
    private GetAlbumHandler getAlbumHandler = new GetAlbumHandler();

    protected static Logger logger = LogManager.getLogger("handlers");

    public String messageReceived(String xmlMessage){
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
                res = newPhotoCommandHandler.handle(message);
                break;
            case ADD_USER_TO_ALBUM:
                res = addUserToAlbumHandler.handle(message);
                break;
            case GET_ALBUM:
                res = getAlbumHandler.handle(message);
            default:
                break;
        }
        return fromClassToXml(res);
    }

    public  <T> T fromXmlToClass(String xml, Class<T> tClass){
        try {
            return XsdUtils.serializeFromXml(xml, tClass);
        } catch (JAXBException ex){
            logger.warn("Failed creating class from xml", ex);
            return null;
        }
    }

    public  <T> String fromClassToXml(T object){
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
        NewAlbumRequestBody requestBody = new NewAlbumRequestBody();

        headerRequest.setCommand(CommandsEnum.CREATE_NEW_ALBUM);
        headerRequest.setUserId("eilon47");
        requestBody.setManager("eilon47");
        requestBody.setAlbumName("eilon_album");
        requestBody.setRules(new Rules());

        requestMessage.setHeader(headerRequest);
        String bodyString = handler.fromClassToXml(requestBody);
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
