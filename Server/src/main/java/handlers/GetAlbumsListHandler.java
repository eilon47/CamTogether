package handlers;

import xmls.*;

import java.sql.ResultSet;


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
            ResultSet rs = dbClient.selectQuery("album_name, participants, creator", "albums");
            AlbumsList albumsList = new AlbumsList();
            while(rs.next()){
                String album = rs.getString("album_name");
                String participants = rs.getString("participants");
                String creator = rs.getString("creator");
                if(participants.contains(request.getHeader().getUsername()) || creator.contains(request.getHeader().getUsername())){
                    CTAlbumPreview ctAlbumPreview = dbClient.getAlbumPreview(album);
                    albumsList.getAlbums().add(ctAlbumPreview);
                }
            }
            responseBody.setAlbums(albumsList);
            dbClient.closeConnection();
            responseMessage.setBody(fromClassToXml(responseBody));
        }catch (Exception e){
            logger.warn("Failed getting albums list ", e);
            responseMessage.getHeader().setCommandSuccess(false);
            return responseMessage;
        }
        return responseMessage;
    }
}
