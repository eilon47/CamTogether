package handlers;

import common.IdGen;
import database.SqlStatements;
import xmls.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;

public class CreateNewAlbumCommandHandler extends CommandHandler{
    @Override
    public ResponseMessage handle(RequestMessage request) {
        logger.info("Handling new album request");
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHeader(createHeaderResponse(request.getHeader()));
        NewAlbumResponseBody responseBody = new NewAlbumResponseBody();
        try {
            NewAlbumRequestBody newAlbumRequest = fromXmlToClass(request.getBody(), NewAlbumRequestBody.class);
            String albumName = newAlbumRequest.getAlbumName();
            String owner = newAlbumRequest.getManager();
            String participants = "";
            Calendar calendar = Calendar.getInstance();
            Date creation = new Date(calendar.getTime().getTime());
            calendar.add(Calendar.DATE, 10);
            Date expiration = new Date(calendar.getTime().getTime());
            Rules rules = newAlbumRequest.getRules();
            responseBody.setAlbumName(albumName);
            responseMessage.setBody(fromClassToXml(responseBody));
            logger.debug("Creating connection to db");
            dbClient.createConnection();
            String[] values = {"", albumName, albumName, userId, userId};
            logger.info("inserting new album with values " + Arrays.toString(values));
            boolean result = dbClient.insertNewRecord(SqlStatements.INSERT_NEW_ALBUM_TO_ALBUMS_TABLE, values);
            if (result){
                logger.info("Creating new table for album " + albumName);
                result = dbClient.createTableFromString(String.format(SqlStatements.NEW_ALBUM_CREATION, albumName));
                logger.debug("creating album " +albumName+ " result " + result);
                boolean location = nullityCheck(rules.getRadius()) && nullityCheck(rules.getLatitude()) && nullityCheck(rules.getLongitude());
                boolean time = nullityCheck(rules.getEndTime()) && nullityCheck(rules.getStartTime());
                Object[] rulesArr = {"", values[1], location, rules.getLongitude(), rules.getLatitude(), rules.getRadius(),
                        time, rules.getStartTime(), rules.getEndTime() };
                logger.info("Adding new rules record for album " + albumName);
                result = dbClient.insertNewRecord(SqlStatements.INSERT_NEW_RULES_TO_RULES_TABLE, rulesArr);
                logger.debug("inserting rules for album " + albumName+ "result " + result);
            }
            logger.debug("closing connection with db");
            dbClient.closeConnection();
        }catch (SQLException | ClassNotFoundException ex) {
            logger.error("Create new album request failed", ex);
            responseMessage.getHeader().setCommandSuccess(false);
        }
        return responseMessage;
    }
    private boolean nullityCheck(Object o){
        return o != null;
    }

}
