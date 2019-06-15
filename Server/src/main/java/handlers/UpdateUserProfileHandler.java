package handlers;

import common.ImageUtils;
import database.SqlStatements;
import xmls.RequestMessage;
import xmls.ResponseMessage;
import xmls.UpdateUserProfileRequestBody;
import xmls.User;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateUserProfileHandler extends CommandHandler {
    @Override
    public ResponseMessage handle(RequestMessage request) {
        logger.info("Updating user profile");
        ResponseMessage response = new ResponseMessage();
        //create header of message
        response.setHeader(createResponseHeader(request.getHeader()));
        UpdateUserProfileRequestBody requestBody = fromXmlToClass(request.getBody(), UpdateUserProfileRequestBody.class);
        User user = requestBody.getUser();
        User old;
        try {
            old = dbClient.getUser(user.getUserName());
            if(emptyOrNull(user.getPassword()))
                user.setPassword(old.getPassword());
            if (emptyOrNull(user.getJoinDate()))
                user.setJoinDate(old.getJoinDate());
            if (emptyOrNull(user.getBirthday()))
                user.setBirthday(old.getBirthday());
            if (emptyOrNull(user.getEmail()))
                user.setEmail(old.getEmail());
        }
        catch (Exception e) {
            logger.warn(e);
        }
        user.setProfileImage(updateImage(user.getProfileImage()));
        try {

            boolean success = dbClient.updateUser(user);
            response.getHeader().setCommandSuccess(success);
            return response;
        } catch (SQLException | ClassNotFoundException e) {
            response.getHeader().setCommandSuccess(false);
            response.getHeader().setReason(e.getMessage());
            return response;
        }
    }


    private byte[] updateImage(byte[] img) {
        byte [] image;
        try {
            ImageUtils imageUtils = new ImageUtils();
            image = imageUtils.createThumbnail(img, ImageUtils.PROFILE_IMG_SIZE, ImageUtils.PROFILE_IMG_SIZE);

        } catch (IOException ex){
            image = null; // TODO add default image
        }
        return image;
    }

    private boolean emptyOrNull(String n){
        return n == null || n.isEmpty();
    }
}
