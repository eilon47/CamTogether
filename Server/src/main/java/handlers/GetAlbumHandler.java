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
            if(!this.isAuthorized(req_body.getAlbumName(), request.getHeader().getUserId())) {
                responseMessage.getHeader().setCommandSuccess(false);
                responseMessage.setBody(fromClassToXml(responseBody));
                return responseMessage;
            }
            String sql = String.format(SqlStatements.SELECT_ALL_RECORDS, req_body.getAlbumName());
            logger.info("Querying " + sql);
            resultSet = dbClient.doSqlStatement(sql);
            while (resultSet.next()) {
                CTImage img = new CTImage();
                img.setAlbumName(req_body.getAlbumName());
                //img.setUserName(resultSet.getString("user_name"));
                img.setImageName(resultSet.getString("image_name"));
                img.setUserID(resultSet.getString("user_id"));
                img.setImageLength(resultSet.getInt("length"));
                img.setImageWidth(resultSet.getInt("width"));
                img.setImageSize(resultSet.getInt("image_size"));
                img.setImageData(resultSet.getBytes("image"));
                logger.debug("Adding image " + img.getImageName() + "to list");
                responseBody.getImages().add(img);
            }
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


    private boolean isAuthorized(String album_id, String user) throws SQLException {
        logger.debug("connection with db created - executing select query to receive participants list");
        ResultSet resultSet = dbClient.selectQuery("participants", "albums", "album_id='" + album_id + "'");
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
