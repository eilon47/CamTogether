package handlers;

import xmls.RequestMessage;
import xmls.ResponseMessage;

public class UpdateUserProfileHandler extends CommandHandler {
    @Override
    public ResponseMessage handle(RequestMessage request) {
        logger.info("Updating user profile");
        ResponseMessage response = new ResponseMessage();
        //create header of message
        response.setHeader(createResponseHeader(request.getHeader()));
        return null;
    }
}
