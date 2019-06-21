package handlers;

import common.ImageUtils;
import database.SqlStatements;
import xmls.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Arrays;


public class GetAlbumsListHandler extends CommandHandler {
    @Override
    public ResponseMessage handle(RequestMessage request) {
        logger.info("Handling get albums list request");
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHeader(createResponseHeader(request.getHeader()));
        GetAlbumsListRequestBody requestBody = fromXmlToClass(request.getBody(), GetAlbumsListRequestBody.class);
        GetAlbumsListResponseBody responseBody = new GetAlbumsListResponseBody();
        try{
            dbClient.createConnection();
            String sql = String.format(SqlStatements.SELECT_FROM_ALBUMS,"album_name, participants, creator","");
            ResultSet rs = dbClient.selectQuery(sql);
            AlbumsList albumsList = new AlbumsList();
            while(rs.next()){
                String album = (rs.getString("album_name"));
                String participants = rs.getString("participants");
                String creator = rs.getString("creator");
                if(participants.contains(request.getHeader().getUsername()) || creator.contains(request.getHeader().getUsername())){
                    CTAlbumPreview ctAlbumPreview = dbClient.getAlbumPreview(album);
                    ctAlbumPreview.setName(ctAlbumPreview.getName().replace("_", " "));
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
