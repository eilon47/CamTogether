package handlers;

import database.SqlStatements;
import xmls.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class GetUserDetailsHandler extends CommandHandler {
    @Override
    public ResponseMessage handle(RequestMessage request) {
        logger.info("Handling login request");
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHeader(createResponseHeader(request.getHeader()));
        GetUserDetailsRequestBody requestBody = fromXmlToClass(request.getBody(), GetUserDetailsRequestBody.class);
        try {
            String username = requestBody.getUsername();
            dbClient.createConnection();
            String[] o = {"", username};
            ResultSet rs = dbClient.selectQuery(SqlStatements.SELECT_USER_FROM_USERS, o);
            if(rs.next()) {
                User user = new User();
                user.setJoinDate(rs.getString("joined"));
                user.setDescription(rs.getString("info"));
                user.setEmail(rs.getString("email"));
                user.setProfileImage(rs.getBytes("profile_img"));
                user.setBirthday(rs.getString("birthday"));
                String friends = rs.getString("friends");
                if (!friends.trim().isEmpty()) {
                    user.getFriends().addAll( Arrays.asList(friends.split(",")));
                }
                user.setUserName(username);
                rs.close();
                GetUserDetailsResponseBody responseBody = new GetUserDetailsResponseBody();
                responseBody.setUser(user);
                responseMessage.setBody(fromClassToXml(responseBody));
                dbClient.closeConnection();
            } else {
                responseMessage.getHeader().setCommandSuccess(false);
                responseMessage.getHeader().setReason("Could not get user details");
            }
        }catch (SQLException  e) {
            responseMessage.getHeader().setCommandSuccess(false);
            responseMessage.getHeader().setReason("Could not get data, cause: SQL");
        }
        catch ( ClassNotFoundException ce) {
            responseMessage.getHeader().setReason(ce.getCause().getMessage());
            responseMessage.getHeader().setCommandSuccess(false);
        }
        return responseMessage;
    }
}
