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
        user.setProfileImage(updateImage(user.getProfileImage()));
        String sql = String.format(SqlStatements.UPDATE_USER_PROFILE, user.getUserName());
        Object[] values = {user.getUserName(), user.getPassword(), user.getBirthday(), user.getJoinDate(),
                user.getProfileImage(), user.getEmail(), user.getFriends(), user.getDescription()};
        try {
            dbClient.createConnection();
            boolean success = dbClient.updateQuery(sql, values);
            dbClient.closeConnection();
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
            image = imageUtils.createThumbnail(img, 30, 30);

        } catch (IOException ex){
            image = null; // TODO add default image
        }
        return image;
    }
}
