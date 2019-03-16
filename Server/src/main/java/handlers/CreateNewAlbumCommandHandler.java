package handlers;

import common.IdGen;
import database.SqlStatements;
import xmls.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CreateNewAlbumCommandHandler extends CommandHandler{
    
    private static boolean nullityCheck(Object o){
        return o != null;
    }

    @Override
    public ResponseMessage handle(RequestMessage request) {
        logger.info("Handling new album request");
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHeader(createHeaderResponse(request.getHeader()));
        NewAlbumResponseBody responseBody = new NewAlbumResponseBody();
        try {
            NewAlbumRequestBody newAlbumRequest = fromXmlToClass(request.getBody(), NewAlbumRequestBody.class);
            String albumName = newAlbumRequest.getAlbumName();
            String userId = newAlbumRequest.getManager();
            Rules rules = newAlbumRequest.getRules();
            responseBody.setAlbumName(albumName);
            responseMessage.setBody(fromClassToXml(responseBody));
            logger.debug("Creating connection to db");
            dbClient.createConnection();
            String[] values = new String[5];
            values[0] = "";
            values[1] = IdGen.generate(albumName);
            values[2] = albumName;
            values[3] = userId;
            values[4] = "";
            logger.info("inserting new album with values " + Arrays.toString(values));
            boolean result = dbClient.insertNewRecord(SqlStatements.INSERT_NEW_ALBUM_TO_ALBUMS_TABLE, values);
            if (result){
                logger.info("Creating new table for album " + albumName);
                boolean res = dbClient.createTableFromString(String.format(SqlStatements.NEW_ALBUM_CREATION, albumName));
                boolean location = nullityCheck(rules.getRadius()) && nullityCheck(rules.getLatitude()) && nullityCheck(rules.getLongitude());
                boolean time = nullityCheck(rules.getEndTime()) && nullityCheck(rules.getStartTime());
                Object[] rulesArr = {"", values[1], location, rules.getLongitude(), rules.getLatitude(), rules.getRadius(),
                        time, rules.getStartTime(), rules.getEndTime() };
                logger.info("Adding new rules record for album " + albumName);
                result = dbClient.insertNewRecord(SqlStatements.INSERT_NEW_RULES_TO_RULES_TABLE, rulesArr);
            }
            logger.debug("closing connection with db");
            dbClient.closeConnection();
        }catch (SQLException | ClassNotFoundException ex) {
            responseMessage.getHeader().setCommandSuccess(false);
        }
        return responseMessage;
    }

}
