package handlers;

import common.ImageUtils;
import database.SqlStatements;
import xmls.*;

import javax.naming.directory.InvalidAttributesException;
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
        returnMessage.setHeader(createResponseHeader(request.getHeader()));
        NewUserRequestBody newUserRequestBody = fromXmlToClass(request.getBody(), NewUserRequestBody.class);
        NewUserResponseBody responseBody = new NewUserResponseBody();;
        User user_to_add = newUserRequestBody.getUser();
        responseBody.setUser(user_to_add.getUserName());
        try {
            try {
                checkIfUserCanRegister(user_to_add);
            } catch (InvalidAttributesException ex){
                returnMessage.getHeader().setCommandSuccess(false);
                responseBody.setUser(ex.getMessage());
                return returnMessage;
            }
            dbClient.createConnection();
            Date joined = new Date((new java.util.Date()).getTime());
            byte[] image = setProfilePicture(user_to_add.getProfileImage());
            user_to_add.setDescription("");
            Object[] values_to_user_table = {"", user_to_add.getUserName(), user_to_add.getPassword(), user_to_add.getBirthday(), joined,
                             image, user_to_add.getEmail(), "" /*friends*/, user_to_add.getDescription()};
            boolean res = dbClient.insertQuery(SqlStatements.INSERT_NEW_USER_TO_USERS_TABLE, values_to_user_table);
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

    public boolean checkIfUserCanRegister(User user) throws InvalidAttributesException{
        if (isUsedAlready("email", user.getEmail()))
            throw new InvalidAttributesException("This email is already used!");
        if (isUsedAlready("username", user.getUserName()))
            throw new InvalidAttributesException("This username is already taken");
        return true;
    }
    private boolean isUsedAlready(String key, String value){
        boolean res = false;
        try {
            dbClient.createConnection();
            String sql = String.format(SqlStatements.SELECT_KEY_FROM_USERS, key, key, value);
            ResultSet resultSet = dbClient.selectQuery(sql);
            res = resultSet.next();
        } catch (SQLException e){
            logger.warn(e);
        } catch (ClassNotFoundException ex){
            logger.warn(ex);
        }
        return res;
    }

    private byte[] setProfilePicture(byte[] img) {
        ImageUtils imageUtils = new ImageUtils();
        byte[] b = new byte[1];
        if (img == null) {
            try {
                return imageUtils.getDefaultProfileImg();
            } catch (IOException e) {
                e.printStackTrace();
                return b;
            }
        }
        byte [] image;
        try {
            image = imageUtils.createThumbnail(img, ImageUtils.PROFILE_IMG_SIZE, ImageUtils.PROFILE_IMG_SIZE);
        } catch (IOException ex){
            try {
                image = imageUtils.getDefaultProfileImg() ;
            } catch (IOException e) {
                e.printStackTrace();
                return b;
            }
        }
        return image;
    }
}
