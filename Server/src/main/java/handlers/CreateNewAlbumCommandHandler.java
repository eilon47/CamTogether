package handlers;

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
        responseMessage.setHeader(createResponseHeader(request.getHeader()));
        NewAlbumResponseBody responseBody = new NewAlbumResponseBody();
        try {
            NewAlbumRequestBody newAlbumRequest = fromXmlToClass(request.getBody(), NewAlbumRequestBody.class);
            CTAlbum album = newAlbumRequest.getAlbum();
            Calendar calendar = Calendar.getInstance();

            String albumName = album.getName().replace(" ", "_");
            String creator = album.getCreator();
            String participants = "";
            String description = album.getDescription();
            Date creation = new Date(calendar.getTime().getTime());
            calendar.add(Calendar.DATE, 10);
            Date expiration = new Date(calendar.getTime().getTime());

            Rules rules = album.getRules();
            responseBody.setAlbumName(albumName);
            responseMessage.setBody(fromClassToXml(responseBody));
            logger.debug("Creating connection to db");
            dbClient.createConnection();
            Object[] values_for_album_table = {"", albumName, creator, participants, description,creation, expiration};
            logger.info("inserting new album with values " + Arrays.toString(values_for_album_table));
            boolean result = dbClient.insertQuery(SqlStatements.INSERT_NEW_ALBUM_TO_ALBUMS_TABLE, values_for_album_table);
            if (result){
                logger.info("Creating new table for album " + albumName);
                String [] tables = SqlStatements.newAlbumCreationSQLs(albumName);
                for (String t : tables)
                    result = dbClient.createTableFromString(t);
                logger.debug("creating album " +albumName+ " result " + result);
                boolean location = nullityCheck(rules.getRadius()) && nullityCheck(rules.getLatitude()) && nullityCheck(rules.getLongitude());
                boolean time = nullityCheck(rules.getEndTime()) && nullityCheck(rules.getStartTime());
                Object[] rulesArr = {"", albumName, location, rules.getLongitude(), rules.getLatitude(), rules.getRadius(),
                        time, rules.getStartTime(), rules.getEndTime() };
                logger.info("Adding new rules record for album " + albumName);
                result = dbClient.insertQuery(SqlStatements.INSERT_NEW_RULES_TO_RULES_TABLE, rulesArr);
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
