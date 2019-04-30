package CamTogether.RestApi.services;
import org.springframework.stereotype.Service;
import xmls.*;

import javax.xml.bind.JAXBException;

@Service
public class AlbumService extends AbstractService implements IAlbumService{

    @Override
    public AlbumsList getAlbums(String userName) {
        RequestMessage message = new RequestMessage();
        HeaderRequest headerRequest = new HeaderRequest();
        GetAlbumsListRequestBody getAlbumsListRequestBody = new GetAlbumsListRequestBody();
        getAlbumsListRequestBody.setUser(userName);
        headerRequest.setCommand(CommandsEnum.GET_ALBUMS_LIST);
        headerRequest.setUsername(userName);
        message.setHeader(headerRequest);
        ResponseMessage responseMessage = messageToServerAndResponse(message, getAlbumsListRequestBody);

        try {
            GetAlbumsListResponseBody responseBody = xmlConverter.serializeFromString(responseMessage.getBody(), GetAlbumsListResponseBody.class);
            return responseBody.getAlbums();
        } catch (JAXBException e) {
            e.printStackTrace();
            logger.warn("Could not serialize to Album list object", e);
            return new AlbumsList();
        }
    }

    @Override
    public CTAlbum getAlbum(String userName,String albumName) {
        RequestMessage message = new RequestMessage();
        HeaderRequest headerRequest = new HeaderRequest();
        headerRequest.setUsername(userName);
        headerRequest.setCommand(CommandsEnum.GET_ALBUM);
        message.setHeader(headerRequest);
        GetAlbumRequestBody requestBody = new GetAlbumRequestBody();
        requestBody.setAlbumName(albumName);
        requestBody.setUser(userName);

        ResponseMessage responseMessage = messageToServerAndResponse(message, requestBody);

        try {
            GetAlbumResponseBody responseBody = xmlConverter.serializeFromString(responseMessage.getBody(), GetAlbumResponseBody.class);
            return responseBody.getAlbum();
        }catch (JAXBException e){
            e.printStackTrace();
            logger.warn("Could not serialize to Album object", e);
            return new CTAlbum();
        }

    }

    @Override
    public String postAlbum(String userName, CTAlbum ctAlbum) {
        RequestMessage message = new RequestMessage();
        HeaderRequest headerRequest = new HeaderRequest();
        headerRequest.setCommand(CommandsEnum.CREATE_NEW_ALBUM);
        headerRequest.setUsername(userName);
        message.setHeader(headerRequest);
        NewAlbumRequestBody requestBody= new NewAlbumRequestBody();
        requestBody.setAlbum(ctAlbum);
        ResponseMessage responseMessage = messageToServerAndResponse(message, requestBody);
        return responseMessage.getHeader().isCommandSuccess() ? "Success!" : "Failed To post album " + ctAlbum.getName();
    }


}
