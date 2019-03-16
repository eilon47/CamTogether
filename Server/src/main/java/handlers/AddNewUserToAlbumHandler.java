package handlers;

import database.SqlStatements;
import xmls.AddUserToAlbumRequestBody;
import xmls.RequestMessage;
import xmls.ResponseMessage;
import xmls.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddNewUserToAlbumHandler extends CommandHandler {
    @Override
    public ResponseMessage handle(RequestMessage request) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHeader(createHeaderResponse(request.getHeader()));
        AddUserToAlbumRequestBody addUserToAlbumRequestBody = fromXmlToClass(request.getBody(), AddUserToAlbumRequestBody.class);
        try {
            boolean user_exists = checkUserExistsAndUnique(addUserToAlbumRequestBody.getUserToAdd());
            if (!user_exists){
                responseMessage.getHeader().setCommandSuccess(false);
                responseMessage.setBody("Failed to find user");
                return responseMessage;
            }
            String new_participants_val = createNewParticipantsValue(addUserToAlbumRequestBody.getUserToAdd(), addUserToAlbumRequestBody.getAddToAlbum());
            boolean res = updateNewValue(new_participants_val, addUserToAlbumRequestBody.getAddToAlbum());
            if (!res){
                responseMessage.getHeader().setCommandSuccess(false);
                responseMessage.setBody("Failed updating user in album's participants");
                return responseMessage;
            }
        } catch (ClassNotFoundException | SQLException ex){
            responseMessage.getHeader().setCommandSuccess(false);
            responseMessage.setBody(ex.getMessage());
            return responseMessage;
        }
        responseMessage.getHeader().setCommandSuccess(true);
        return responseMessage;
    }

    private boolean checkUserExistsAndUnique(User user) throws SQLException, ClassNotFoundException{
        //Check the User exists in the users table
        dbClient.createConnection();
        String[] is_user_exist_values = {"",user.getUserID()};
        ResultSet resultSet = dbClient.prepareStatementAllStrings(SqlStatements.SELECT_USER_FROM_USERS, is_user_exist_values);
        boolean has_user = resultSet.next() && !resultSet.next(); //Checks we have only 1 user
        resultSet.close();
        dbClient.closeConnection();
        return has_user;
    }

    public String createNewParticipantsValue(User user, String album_id) throws SQLException, ClassNotFoundException{
        dbClient.createConnection();
        ResultSet resultSet = dbClient.selectQuery("participants", "albums" , "album_id='"+ album_id +"'");
        String participants = null;
        if (resultSet.next())
          participants = resultSet.getString("participants");
        resultSet.close();
        dbClient.closeConnection();
        if(participants == null || participants.isEmpty())
            return user.getUserName();
        if(participants.contains(user.getUserName()))
            return participants;
        participants = participants + "," + user.getUserName();
        return participants;
    }

    private boolean updateNewValue(String newParticipantsStr, String album_id) throws SQLException, ClassNotFoundException{
        String sql = String.format(SqlStatements.UPDATE_TABLE, "albums", "participants", newParticipantsStr, "album_id='"+ album_id + "'" );
        dbClient.createConnection();
        boolean res = dbClient.updateQuery(sql);
        dbClient.closeConnection();
        return res;
    }

}
