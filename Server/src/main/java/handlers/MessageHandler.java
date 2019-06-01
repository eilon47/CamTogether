package handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xmls.*;
import converters.XmlConverter;
import javax.xml.bind.JAXBException;

public class MessageHandler {
    private XmlConverter converter = new XmlConverter();

    private CreateNewAlbumCommandHandler createNewAlbumCommandHandler = new CreateNewAlbumCommandHandler();
    private NewPhotoCommandHandler newPhotoCommandHandler = new NewPhotoCommandHandler();
    private AddUserToAlbumHandler addUserToAlbumHandler = new AddUserToAlbumHandler();
    private GetAlbumHandler getAlbumHandler = new GetAlbumHandler();
    private GetAlbumsListHandler getAlbumsListHandler = new GetAlbumsListHandler();
    private GetImageHandler getImageHandler = new GetImageHandler();
    private UpdateAlbumRulesHandler updateAlbumRulesHandler = new UpdateAlbumRulesHandler();
    private CreateNewUserHandler createNewUserHandler = new CreateNewUserHandler();
    private UpdateUserProfileHandler updateUserProfileHandler = new UpdateUserProfileHandler();
    private LoginUserHandler loginUserHandler = new LoginUserHandler();
    private GetUserDetailsHandler getUserDetailsHandler = new GetUserDetailsHandler();
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
            case ADD_NEW_PHOTO_TO_ALBUM:
                res = newPhotoCommandHandler.handle(message);
                break;
            case ADD_USER_TO_ALBUM:
                res = addUserToAlbumHandler.handle(message);
                break;
            case GET_ALBUM:
                res = getAlbumHandler.handle(message);
                break;
            case GET_ALBUMS_LIST:
                res = getAlbumsListHandler.handle(message);
                break;
            case GET_IMAGE:
                res = getImageHandler.handle(message);
                break;
            case UPDATE_ALBUM_RULES:
                res = updateAlbumRulesHandler.handle(message);
                break;
            case CREATE_NEW_USER:
                res = createNewUserHandler.handle(message);
                break;
            case UPDATE_USER_PROFILE:
                res = updateUserProfileHandler.handle(message);
                break;
            case LOGIN_WITH_USER:
                res = loginUserHandler.handle(message);
                break;
            case GET_USER_DETAILS:
                res = getUserDetailsHandler.handle(message);
            default:
                break;
        }
        return fromClassToXml(res);
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

    public static void main(String [] a){
//        MessageHandler handler = new MessageHandler();
//        RequestMessage requestMessage = new RequestMessage();
//        RequestHeader headerRequest = new RequestHeader();
//        NewAlbumRequestBody requestBody = new NewAlbumRequestBody();
//
//        headerRequest.setCommand(CommandsEnum.CREATE_NEW_ALBUM);
//        headerRequest.setUserId("eilon47");
//        requestBody.setManager("eilon47");
//        requestBody.setAlbumName("eilon_album");
//        requestBody.setRules(new Rules());
//
//        requestMessage.setHeader(headerRequest);
//        String bodyString = handler.fromClassToXml(requestBody);
//        requestMessage.setBody(bodyString);
//        requestMessage.setHeader(headerRequest);
//        String xml = handler.fromClassToXml(requestMessage);
//        try {
//            handler.messageReceived(xml);
//        } catch (Exception e){
//            logger.warn(e);
//        }
    }

}
