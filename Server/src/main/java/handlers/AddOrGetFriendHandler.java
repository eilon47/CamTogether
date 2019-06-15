package handlers;
import database.SqlStatements;
import xmls.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddOrGetFriendHandler extends CommandHandler {
    @Override
    public ResponseMessage handle(RequestMessage request) {
        logger.info("Handling Add user request");
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHeader(createResponseHeader(request.getHeader()));
        AddOrGetFriendRequestBody requestBody = fromXmlToClass(request.getBody(), AddOrGetFriendRequestBody.class);
        try {
            boolean user_exists = checkUserExistsAndUnique(requestBody.getUsername());
            if (!user_exists) {
                logger.warn("User is not exists, can't add it to friends");
                responseMessage.getHeader().setCommandSuccess(false);
                responseMessage.setBody("Failed to find user");
                return responseMessage;
            }
            User requester = dbClient.getUser(request.getHeader().getUsername());
            User toAdd = dbClient.getUser(requestBody.getUsername());
            if(requester.getFriends().contains(requestBody.getUsername())) {
                responseMessage.getHeader().setCommandSuccess(false);
                responseMessage.setBody("User "+ requestBody.getUsername() +" already exists in " + requester.getUserName() + " friends list");
                return responseMessage;
            }
            requester.getFriends().add(requestBody.getUsername());
            boolean success = dbClient.updateUser(requester);
            responseMessage.getHeader().setCommandSuccess(success);
            if(toAdd.getFriends().contains(requester.getUserName())) {
                responseMessage.getHeader().setCommandSuccess(false);
                responseMessage.setBody("User "+ requestBody.getUsername() +" already exists in " + requester.getUserName() + " friends list");
                return responseMessage;
            }
            toAdd.getFriends().add(requester.getUserName());
            success = dbClient.updateUser(toAdd);
            responseMessage.getHeader().setCommandSuccess(success);
            return responseMessage;

        } catch (ClassNotFoundException | SQLException ex) {
            logger.error("Failed adding user " + requestBody.getUsername() + " to friends of " + request.getHeader().getUsername(), ex);
            responseMessage.getHeader().setCommandSuccess(false);
            responseMessage.setBody("Request failed");
            return responseMessage;
        }
    }

    private boolean checkUserExistsAndUnique(String user) throws SQLException, ClassNotFoundException {
        //Check the User exists in the users table
        logger.debug("Checking if user " + user + " exists");
        dbClient.createConnection();
        logger.debug("Connection to db was created");
        String[] is_user_exist_values = {"", user};
        ResultSet resultSet = dbClient.selectQuery(SqlStatements.SELECT_USER_FROM_USERS, is_user_exist_values);
        boolean has_user = resultSet.next();
        boolean has_more_users = resultSet.next(); //Checks we have only 1 user
        if (has_more_users)
            throw new SQLException("Expected to get only one user! many us");
        logger.info("User " + user + " " + user + " exists result=" + has_user);
        resultSet.close();
        dbClient.closeConnection();
        return has_user;
    }
}
