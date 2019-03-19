package handlers;

import xmls.GetAlbumsListRequestBody;
import xmls.GetAlbumsListResponseBody;
import xmls.RequestMessage;
import xmls.ResponseMessage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GetAlbumsListHandler extends CommandHandler {
    @Override
    public ResponseMessage handle(RequestMessage request) {
        logger.info("Handling Add user request");
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHeader(createHeaderResponse(request.getHeader()));
        GetAlbumsListRequestBody requestBody = fromXmlToClass(request.getBody(), GetAlbumsListRequestBody.class);
        GetAlbumsListResponseBody responseBody = new GetAlbumsListResponseBody();
        try{
            dbClient.createConnection();
            ResultSet rs = dbClient.selectQuery("album_name, participants", "albums");
            while(rs.next()){
                String album = rs.getString("album_name");
                String participants = rs.getString("participants");
                if(participants.contains(request.getHeader().getUserId())){
                    responseBody.getAlbums().add(album);
                }
            }
            responseMessage.setBody(fromClassToXml(responseBody));
        }catch (Exception e){
            logger.warn("Failed getting albums list ", e);
            responseMessage.getHeader().setCommandSuccess(false);
            return responseMessage;
        }
        return responseMessage;
    }
}
