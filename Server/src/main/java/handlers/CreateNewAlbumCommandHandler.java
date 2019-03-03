package handlers;

import database.DBClient;
import xsd.Message;
import xsd.Rules;
import xsd.albums.NewAlbumRequestBody;

import javax.xml.bind.JAXBException;
import java.sql.SQLException;

public class CreateNewAlbumCommandHandler extends CommandHandler{

    //TODO Autowiring
    DBClient dbclient;


    @Override
    public Message handle(Message request) throws JAXBException {
        NewAlbumRequestBody newAlbumRequest = serializeFromXml(request.getBody().toString(), NewAlbumRequestBody.class);
        String albumName = newAlbumRequest.getAlbumName();
        String userId = newAlbumRequest.getUserId();
        Rules rules = newAlbumRequest.getRules();
        try {
            boolean result = dbclient.insertOneRecord("albums", albumName, userId);
            if (result){
                result = dbclient.createTable(albumName, "image ", "user_id", "");
                if(!result)
                    //TODO rollback
                    return new Message();
                //String[] sep_rules = rules("#");
            }
        }catch (SQLException | ClassNotFoundException ex) {
            return new Message();
        }
        return new Message();
    }
}
