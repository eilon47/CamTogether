package handlers;

import common.ImageUtils;
import database.SqlStatements;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import xmls.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateNewUserHandler extends CommandHandler {
    @Override
    public ResponseMessage handle(RequestMessage request) {
        logger.info("Handling new user request");
        ResponseMessage returnMessage = new ResponseMessage();
        //create header of message
        returnMessage.setHeader(createHeaderResponse(request.getHeader()));
        NewUserRequestBody newUserRequestBody = fromXmlToClass(request.getBody(), NewUserRequestBody.class);
        NewUserResponseBody responseBody = new NewUserResponseBody();;
        User user_to_add = newUserRequestBody.getUser();
        responseBody.setUser(user_to_add.getUserName());
        try {
            try {
                checkIfUserCanRegister(user_to_add);
            } catch (ValueException ex){
                returnMessage.getHeader().setCommandSuccess(false);
                responseBody.setUser(ex.getMessage());
                return returnMessage;
            }
            dbClient.createConnection();
            Date joined = new Date((new java.util.Date()).getTime());
            byte[] image = setProfilePicture(user_to_add.getProfileImage());
            Object[] values_to_user_table = {"", user_to_add.getUserName(), user_to_add.getPassword(), user_to_add.getBirthday(), joined,
                             image, user_to_add.getEmail(), "" /*friends*/, user_to_add.getDescription()};
            boolean res = dbClient.dynamicQuery(SqlStatements.INSERT_NEW_USER_TO_USERS_TABLE, values_to_user_table);
            if (!res)
                returnMessage.getHeader().setCommandSuccess(false);
            dbClient.closeConnection();
            return returnMessage;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        returnMessage.getHeader().setCommandSuccess(false);
        return returnMessage;

    }

    public boolean checkIfUserCanRegister(User user) throws ValueException{
        if (isUsedAlready("email", user.getEmail()))
            throw new ValueException("This email is already used!");
        if (isUsedAlready("username", user.getUserName()))
            throw new ValueException("This username is already taken");
        return true;
    }
    private boolean isUsedAlready(String key, String value){
        boolean res = false;
        try {
            dbClient.createConnection();
            String sql = String.format(SqlStatements.SELECT_KEY_FROM_USERS, key, key, value);
            ResultSet resultSet = dbClient.doSqlStatement(sql);
            res = resultSet.next();
        } catch (SQLException e){
            logger.warn(e);
        } catch (ClassNotFoundException ex){
            logger.warn(ex);
        }
        return res;
    }

    private byte[] setProfilePicture(byte[] img) {
        byte [] image;
        try {
            ImageUtils imageUtils = new ImageUtils();
            image = imageUtils.createThumbnail(img, 30, 30);

        } catch (IOException ex){
            image = null; // TODO add default image
        }
        return image;
    }
}
