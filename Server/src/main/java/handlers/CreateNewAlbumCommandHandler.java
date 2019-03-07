package handlers;

import common.IdGen;
import database.DBClient;
import database.SqlStatements;
import xsd.Header;
import xsd.Message;
import xsd.Rules;
import xsd.albums.NewAlbumRequestBody;
import xsd.albums.NewAlbumResponseBody;

import javax.xml.bind.JAXBException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CreateNewAlbumCommandHandler extends CommandHandler{

    DBClient dbclient = new DBClient();


    @Override
    public Message handle(Message request) {
        Message retMessage = new Message();
        Header header = new Header();
        header.setCommand(request.getHeader().getCommand());
        header.setFromUserId(request.getHeader().getFromUserId());
        retMessage.setHeader(header);
        NewAlbumResponseBody responseBody = new NewAlbumResponseBody();

        try {
        NewAlbumRequestBody newAlbumRequest = serializeFromXml(request.getBody().toString(), NewAlbumRequestBody.class);
        String albumName = newAlbumRequest.getAlbumName();
        responseBody.setAlbumName(albumName);
        String userId = newAlbumRequest.getUserId();
        Rules rules = newAlbumRequest.getRules();
            dbclient.createConnection();
            String sql = SqlStatements.INSERT_NEW_ALBUM_TO_ALBUMS_TABLE;
            Object[] values = new Object[4];
            values[0] = IdGen.generate(albumName);
            values[1] = albumName;
            values[2] = userId;
            values[3] = "";
            ResultSet resultSet = dbclient.dynamicPrepareStatement(sql,values);
            if (!resultSet.wasNull()){
                resultSet = dbclient.createTableFromString(String.format(SqlStatements.NEW_ALBUM_CREATION, albumName));
                boolean location = nullityCheck(rules.getRadius()) && nullityCheck(rules.getLatitude()) && nullityCheck(rules.getLongitude());
                boolean time = nullityCheck(rules.getEnd_date()) && nullityCheck(rules.getStart_date()) &&
                        nullityCheck(rules.getEnd_hour()) && nullityCheck(rules.getStart_hour());
                Object[] rulesArr = {
                    values[0], location, rules.getLongitude(), rules.getLatitude(), rules.getRadius(),
                        time, rules.getStart_date(), rules.getEnd_date(), rules.getStart_hour(), rules.getEnd_hour()
                };
                resultSet = dbclient.dynamicPrepareStatement(SqlStatements.INSERT_NEW_RULES_TO_RULES_TABLE, rulesArr);
            }
        }catch (SQLException | JAXBException | ClassNotFoundException ex) {
            responseBody.setSucceeded(true);
            retMessage.setBody(responseBody);
            return retMessage;
        }
        responseBody.setSucceeded(false);
        retMessage.setBody(responseBody);
        return retMessage;
    }

    private static boolean nullityCheck(Object o){
        return o == null;
    }
}
