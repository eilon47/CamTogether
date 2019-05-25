package handlers;

import database.SqlStatements;
import xmls.*;

import java.sql.SQLException;

public class GetImageHandler extends CommandHandler {
    @Override
    public ResponseMessage handle(RequestMessage request) {
        logger.info("Handling new user request");
        ResponseMessage returnMessage = new ResponseMessage();
        //create header of message
        returnMessage.setHeader(createResponseHeader(request.getHeader()));
        GetImageRequestBody requestBody = fromXmlToClass(request.getBody(), GetImageRequestBody.class);
        String album = requestBody.getAlbum();
        String image = requestBody.getImageName();
        String username = requestBody.getUsername();

        try {
            if (isAuthorized(album, username)) {
                dbClient.createConnection();
                CTImage img = dbClient.getImageFromAlbum(album, image);
                dbClient.closeConnection();
                GetImageResponseBody responseBody = new GetImageResponseBody();
                responseBody.setImage(img);
                returnMessage.setBody(fromClassToXml(responseBody));
                return returnMessage;
            }
            else {
                returnMessage.getHeader().setCommandSuccess(false);
                returnMessage.setBody("User is not authorized in this album");
                return returnMessage;
            }
        } catch (SQLException | ClassNotFoundException e ) {
            e.printStackTrace();
            returnMessage.getHeader().setCommandSuccess(false);
            returnMessage.setBody("Failed getting image, reason : " + e.getMessage());
            return returnMessage;
        }
    }
}
