package handlers;

//import xsd.CommandRequest;
import database.DBClient;
import database.SqlStatements;
import xmls.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewPhotoCommandHandler extends CommandHandler {

    DBClient db_client = new DBClient();

    @Override
    public ResponseMessage handle(RequestMessage request){
        ResponseMessage returnMessage = new ResponseMessage();
        //create header of message
        HeaderResponse header = new HeaderResponse();
        header.setCommand(request.getHeader().getCommand());
        header.setUserId(request.getHeader().getUserId());
        returnMessage.setHeader(header);

        NewImageRequestBody req_body = fromXmlToClass(request.getBody(), NewImageRequestBody.class);

        NewImageResponseBody resbody = new NewImageResponseBody();;
        resbody.setAlbum(req_body.getAlbum());
        resbody.setImage(req_body.getImage().getImageName());
        try {
            CTImage CTimage = new CTImage();
            CTimage.setUserName(header.getUserId());
            CTimage.setAlbumName(req_body.getAlbum());
            CTimage.setImageName(req_body.getImage().getImageName());
            CTimage.setImageData(req_body.getImage().getImageData());
            CTimage.setImageSize(req_body.getImage().getImageSize());
            if (!alreadyExist(CTimage.getAlbumName(),CTimage.getImageName())) {

                db_client.createConnection();
                String sql = String.format(SqlStatements.INSERT_NEW_IMAGE_TO_ALBUM,CTimage.getAlbumName());
                Object[] values = new Object[7];
                values[0] = "";
                values[1] = req_body.getImage().getImageName();
                values[2] = req_body.getImage().getImageSize();
                values[3] = req_body.getImage().getImageData();
                values[4] = CTimage.getUserName();
                values[5] = CTimage.getImageLength();
                values[6] = CTimage.getImageWidth();

                boolean res = db_client.dynamicPrepareStatement(sql,values);
                if (!res)

                return returnMessage;
            }
        } catch (SQLException | ClassNotFoundException e){
            //resbody.setSucceeded(true);
            //returnMessage.setBody(resbody);
            return returnMessage;
        }


        return null;
    }

    public boolean alreadyExist(String album_name, String image_name){
        return false;
    }

}

