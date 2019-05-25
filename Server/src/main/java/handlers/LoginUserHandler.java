package handlers;

import database.SqlStatements;
import xmls.LoginRequestBody;
import xmls.RequestMessage;
import xmls.ResponseMessage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginUserHandler extends CommandHandler {
    @Override
    public ResponseMessage handle(RequestMessage request) {
        logger.info("Handling login request");
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHeader(createResponseHeader(request.getHeader()));

        LoginRequestBody requestBody = fromXmlToClass(request.getBody(), LoginRequestBody.class);
        try{
            dbClient.createConnection();
            String sql = SqlStatements.SELECT_USER_FROM_USERS;
            Object[] args = {"", requestBody.getUsername()};
            ResultSet rs = dbClient.selectQuery(sql, args);
            if(rs.next()) {
                String username = rs.getString("username");

                String password = rs.getString("password");
                if(password.equals(requestBody.getPassword()) && username.trim().equals(requestBody.getUsername())) {
                    logger.info("login to user " + username + " succeeded");
                    responseMessage.getHeader().setCommandSuccess(true);
                } else {
                    logger.warn("Trying to login to user " +requestBody.getUsername()+"with wrong password");
                    responseMessage.getHeader().setCommandSuccess(false);
                    responseMessage.getHeader().setReason("Password does not match");
                }
            }else {
                logger.warn("trying to login with user that is not exists " + requestBody.getUsername());
                responseMessage.getHeader().setCommandSuccess(false);
                responseMessage.getHeader().setReason(String.format("User with username %s is not exists", requestBody.getUsername()));
                return responseMessage;
            }
            dbClient.closeConnection();
            return responseMessage;
        } catch (SQLException | ClassNotFoundException e) {
            responseMessage.getHeader().setCommandSuccess(false);
            responseMessage.getHeader().setReason("An error occurred");
            logger.warn("Error while handling login ", e);
            return responseMessage;
        }
    }
}
