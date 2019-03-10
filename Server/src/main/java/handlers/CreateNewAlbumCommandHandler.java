package handlers;

import common.IdGen;
import database.DBClient;
import database.SqlStatements;
import xmls.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CreateNewAlbumCommandHandler extends CommandHandler{

    private static DBClient dbclient = new DBClient();

    private static boolean nullityCheck(Object o){
        return o == null;
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
            dbclient.createConnection();
            String sql = SqlStatements.INSERT_NEW_ALBUM_TO_ALBUMS_TABLE;
            Object[] values = new Object[4];
            values[0] = IdGen.generate(albumName);
            values[1] = albumName;
            values[2] = userId;
            values[3] = "";
            logger.info("inserting new album with values " + Arrays.toString(values));
            ResultSet resultSet = dbclient.dynamicPrepareStatement(sql,values);
            if (!resultSet.wasNull()){
                logger.info("Creating new table for album " + albumName);
                boolean res = dbclient.createTableFromString(String.format(SqlStatements.NEW_ALBUM_CREATION, albumName));
                boolean location = nullityCheck(rules.getRadius()) || nullityCheck(rules.getLatitude()) || nullityCheck(rules.getLongitude());
                boolean time = nullityCheck(rules.getEndTime()) || nullityCheck(rules.getStartTime());
                Object[] rulesArr = {
                    values[0], location, rules.getLongitude(), rules.getLatitude(), rules.getRadius(),
                        time, rules.getStartTime(), rules.getEndTime() };
                logger.info("Adding new rules record for album " + albumName);
                resultSet = dbclient.dynamicPrepareStatement(SqlStatements.INSERT_NEW_RULES_TO_RULES_TABLE, rulesArr);
            }
            logger.debug("closing connection with db");
            dbclient.closeConnection();
        }catch (SQLException | ClassNotFoundException ex) {
            responseMessage.getHeader().setCommandSuccess(false);
        }

        return responseMessage;
    }
}
