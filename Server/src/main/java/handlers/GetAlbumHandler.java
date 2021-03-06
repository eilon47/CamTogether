package handlers;


import xmls.*;

import java.sql.SQLException;

public class GetAlbumHandler extends CommandHandler {
    @Override
    public ResponseMessage handle(RequestMessage request) {
        logger.info("Handling get album request");
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHeader(createResponseHeader(request.getHeader()));
        GetAlbumRequestBody req_body = fromXmlToClass(request.getBody(), GetAlbumRequestBody.class);
        req_body.setAlbumName(req_body.getAlbumName().replace(" ", "_"));
        GetAlbumResponseBody responseBody = new GetAlbumResponseBody();
        try {
            logger.debug("Creating connection to db");
            if(!this.isAuthorized(req_body.getAlbumName(), request.getHeader().getUsername())) {
                responseMessage.getHeader().setCommandSuccess(false);
                responseMessage.setBody(fromClassToXml(responseBody));
                return responseMessage;
            }
            dbClient.createConnection();
            CTAlbum album = dbClient.getAlbumWithoutImages(req_body.getAlbumName());
            album.setName(album.getName().replace("_", " "));
            responseBody.setAlbum(album);
            responseMessage.setBody(fromClassToXml(responseBody));
            logger.debug("closing connection with db");
            dbClient.closeConnection();
        } catch (ClassNotFoundException | NullPointerException | SQLException e) {
            logger.error("get album request failed", e);
            responseMessage.getHeader().setCommandSuccess(false);
            return responseMessage;
        }
        return responseMessage;
    }



}
