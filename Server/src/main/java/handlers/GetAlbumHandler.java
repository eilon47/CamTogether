package handlers;


import database.SqlStatements;
import xmls.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetAlbumHandler extends CommandHandler {
    @Override
    public ResponseMessage handle(RequestMessage request) {
        logger.info("Handling get album request");
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHeader(createHeaderResponse(request.getHeader()));
        GetAlbumRequestBody req_body = fromXmlToClass(request.getBody(), GetAlbumRequestBody.class);
        GetAlbumResponseBody responseBody = new GetAlbumResponseBody();
        ResultSet resultSet = null;
        try {
            logger.debug("Creating connection to db");
            dbClient.createConnection();
            if(!this.isAuthorized(req_body.getAlbumName(), request.getHeader().getUsername())) {
                responseMessage.getHeader().setCommandSuccess(false);
                responseMessage.setBody(fromClassToXml(responseBody));
                return responseMessage;
            }
            CTAlbum album = dbClient.getAlbum(req_body.getAlbumName());
            responseBody.setAlbum(album);
            responseMessage.setBody(fromClassToXml(responseBody));
            logger.debug("closing connection with db");
            resultSet.close();
            dbClient.closeConnection();
        } catch (ClassNotFoundException | NullPointerException | SQLException e) {
            logger.error("get album request failed", e);
            responseMessage.getHeader().setCommandSuccess(false);
            return responseMessage;
        }
        return responseMessage;
    }


    private boolean isAuthorized(String album_name, String user) throws SQLException {
        logger.debug("connection with db created - executing select query to receive participants list");
        ResultSet resultSet = dbClient.selectQuery("participants", "albums", "album_name='" + album_name + "'");
        String participants = null;
        boolean res = false;
        if (resultSet.next())
            participants = resultSet.getString("participants");
            if(participants!=null)
                res = participants.contains(user);
        resultSet.close();
        dbClient.closeConnection();
        return res;
    }
}
