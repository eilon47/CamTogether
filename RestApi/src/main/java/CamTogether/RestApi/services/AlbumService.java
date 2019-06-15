package CamTogether.RestApi.services;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xmls.*;

import javax.xml.bind.JAXBException;

@Service
public class AlbumService extends AbstractService implements IAlbumService{

    @Override
    public ResponseEntity<CTAlbum> getAlbum(RequestHeader header, String albumName) {
        RequestMessage message = new RequestMessage();
        message.setHeader(header);
        GetAlbumRequestBody requestBody = new GetAlbumRequestBody();
        requestBody.setAlbumName(albumName);
        requestBody.setUser(header.getUsername());
        ResponseMessage responseMessage = messageToServerAndResponse(message, requestBody);
        if(!responseMessage.getHeader().isCommandSuccess())
            return ResponseEntity.badRequest().header(responseMessage.getHeader().getReason()).body(null);
        try {
            GetAlbumResponseBody responseBody = xmlConverter.serializeFromString(responseMessage.getBody(), GetAlbumResponseBody.class);
            if(responseBody.getAlbum() == null)
                return ResponseEntity.noContent().header("Album was not found").build();
            return ResponseEntity.ok( responseBody.getAlbum());
        }catch (JAXBException e){
            e.printStackTrace();
            logger.warn("Could not serialize to Album object", e);
            return ResponseEntity.noContent().header("Could not parse album").build();
        }
    }

    @Override
    public ResponseEntity<AlbumsList> getAlbums(RequestHeader header) {
        RequestMessage message = new RequestMessage();
        GetAlbumsListRequestBody getAlbumsListRequestBody = new GetAlbumsListRequestBody();
        getAlbumsListRequestBody.setUser(header.getUsername());
        message.setHeader(header);
        ResponseMessage responseMessage = messageToServerAndResponse(message, getAlbumsListRequestBody);
        if(!responseMessage.getHeader().isCommandSuccess()){
            return ResponseEntity.badRequest().header(responseMessage.getHeader().getReason()).body(null);
        }
        try {
            GetAlbumsListResponseBody responseBody = xmlConverter.serializeFromString(responseMessage.getBody(), GetAlbumsListResponseBody.class);
            if(responseBody.getAlbums() == null)
                return ResponseEntity.noContent().header("Could not find albums").build();
            return ResponseEntity.ok(responseBody.getAlbums());
        } catch (JAXBException e) {
            e.printStackTrace();
            logger.warn("Could not serialize to Album list object", e);
            return ResponseEntity.badRequest().header("Could not create albums").build();
        }
    }

    @Override
    public ResponseEntity<String> postAlbum(RequestHeader header, CTAlbum ctAlbum) {
        RequestMessage message = new RequestMessage();
        message.setHeader(header);
        NewAlbumRequestBody requestBody= new NewAlbumRequestBody();
        requestBody.setAlbum(ctAlbum);
        ResponseMessage responseMessage = messageToServerAndResponse(message, requestBody);
        if(responseMessage.getHeader().isCommandSuccess()){
            return ResponseEntity.ok("OKed");
        }
        return ResponseEntity.badRequest().body(responseMessage.getHeader().getReason());
    }

    @Override
    public ResponseEntity<String> addUserToAlbum(RequestHeader header, String userToAdd, String album) {
        RequestMessage message = new RequestMessage();
        message.setHeader(header);

        AddUserToAlbumRequestBody requestBody = new AddUserToAlbumRequestBody();
        requestBody.setUserToAdd(userToAdd);
        requestBody.setAddToAlbum(album);
        ResponseMessage responseMessage = messageToServerAndResponse(message, requestBody);
        if(responseMessage.getHeader().isCommandSuccess()){
            return ResponseEntity.ok("User was added successfully");
        }
        return ResponseEntity.badRequest().body(responseMessage.getHeader().getReason());
    }

    @Override
    public ResponseEntity<String> updateRules(RequestHeader header, String album, Rules rules) {
        RequestMessage message = new RequestMessage();
        message.setHeader(header);
        UpdateRulesRequestBody requestBody = new UpdateRulesRequestBody();
        requestBody.setNewRules(rules);
        requestBody.setAlbum(album);
        ResponseMessage responseMessage = messageToServerAndResponse(message, requestBody);
        if(responseMessage.getHeader().isCommandSuccess()){
            return ResponseEntity.ok("Rules were updated successfully");
        }
        return ResponseEntity.badRequest().body(responseMessage.getHeader().getReason());
    }
}
