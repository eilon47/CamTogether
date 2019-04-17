package handlers;

import database.SqlStatements;
import xmls.AddUserToAlbumRequestBody;
import xmls.RequestMessage;
import xmls.ResponseMessage;
import xmls.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddUserToAlbumHandler extends CommandHandler {
    @Override
    public ResponseMessage handle(RequestMessage request) {
        logger.info("Handling Add user request");
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHeader(createHeaderResponse(request.getHeader()));
        AddUserToAlbumRequestBody addUserToAlbumRequestBody = fromXmlToClass(request.getBody(), AddUserToAlbumRequestBody.class);
        try {
            boolean user_exists = checkUserExistsAndUnique(addUserToAlbumRequestBody.getUserToAdd());
            if (!user_exists) {
                logger.warn("User is not exists, can't add it to album");
                responseMessage.getHeader().setCommandSuccess(false);
                responseMessage.setBody("Failed to find user");
                return responseMessage;
            }
            String new_participants_val = createNewParticipantsValue(addUserToAlbumRequestBody.getUserToAdd(), addUserToAlbumRequestBody.getAddToAlbum());
            boolean res = updateNewValue(new_participants_val, addUserToAlbumRequestBody.getAddToAlbum());
            if (!res) {
                logger.warn("Failed updating user in album's participants");
                responseMessage.getHeader().setCommandSuccess(false);
                responseMessage.setBody("Failed updating user in album's participants");
                return responseMessage;
            }
        } catch (ClassNotFoundException | SQLException ex) {
            logger.error("Failed adding user " + addUserToAlbumRequestBody.getUserToAdd().getUserName() + " to album", ex);
            responseMessage.getHeader().setCommandSuccess(false);
            responseMessage.setBody("Request failed");
            return responseMessage;
        }

        return responseMessage;
    }

    private boolean checkUserExistsAndUnique(User user) throws SQLException, ClassNotFoundException {
        //Check the User exists in the users table
        logger.debug("Checking if user " + user.getUserName() + " exists");
        dbClient.createConnection();
        logger.debug("Connection to db was created");
        String[] is_user_exist_values = {"", user.getUserName()};
        ResultSet resultSet = dbClient.prepareStatementAllStrings(SqlStatements.SELECT_USER_FROM_USERS, is_user_exist_values);
        boolean has_user = resultSet.next();
        boolean has_more_users = resultSet.next(); //Checks we have only 1 user
        if (has_more_users)
            throw new SQLException("Expected to get only one user! many us");
        logger.info("User " + user.getUserName() + " " + user.getUserName() + " exists result=" + has_user);
        resultSet.close();
        dbClient.closeConnection();
        return has_user;
    }

    public String createNewParticipantsValue(User user, String album_name) throws SQLException, ClassNotFoundException {
        dbClient.createConnection();
        logger.debug("connection with db created - executing select query to receive participants list");
        ResultSet resultSet = dbClient.selectQuery("participants", "albums", "album_name='" + album_name + "'");
        String participants;
        if (resultSet.next())
            participants = resultSet.getString("participants");
        else {
            resultSet.close();
            dbClient.closeConnection();
            throw new SQLException("Returned with 0 records");
        }
        resultSet.close();
        dbClient.closeConnection();
        if (participants.isEmpty()) {
            logger.debug("participants list was empty, adding user " + user.getUserName());
            return user.getUserName();
        }
        if (participants.contains(user.getUserName())) {
            logger.debug("participants list already contained user " + user.getUserName());
            return participants;
        }
        participants = participants + "," + user.getUserName();
        return participants;
    }

    private boolean updateNewValue(String newParticipantsStr, String album_name) throws SQLException, ClassNotFoundException {
        String sql = String.format(SqlStatements.UPDATE_TABLE, "albums", "participants", newParticipantsStr, "album_name='" + album_name + "'");
        logger.debug("Updating album's participants value: [" + sql + "]");
        dbClient.createConnection();
        logger.debug("connection with db created - executing query");
        boolean res = dbClient.updateQuery(sql);
        dbClient.closeConnection();
        logger.info("Updated " + album_name + " participants list");
        return res;
    }

}