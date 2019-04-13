package handlers;

import client.CVClient;
import database.SqlStatements;
import xmls.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NewPhotoCommandHandler extends CommandHandler {
    private CVClient cvClient = new CVClient();
    @Override
    public ResponseMessage handle(RequestMessage request){
        logger.info("Handling new photo");
        ResponseMessage returnMessage = new ResponseMessage();
        //create header of message
        returnMessage.setHeader(createHeaderResponse(request.getHeader()));
        NewImageRequestBody req_body = fromXmlToClass(request.getBody(), NewImageRequestBody.class);
        NewImageResponseBody responseBody = new NewImageResponseBody();;
        responseBody.setAlbum(req_body.getAlbum());
        responseBody.setImage(req_body.getImage().getImageName());
        try {
            CTImage CTimage = req_body.getImage();
            boolean cv_res = this.cvClient.queryCvServer(CTimage);
            if(!cv_res){
                logger.info("CV server returned false - not inserting image to db");
                returnMessage.getHeader().setCommandSuccess(false);
                returnMessage.setBody(fromClassToXml(responseBody));
                return returnMessage;
            }
            if (!alreadyExist(CTimage.getAlbumName(),CTimage.getImageName())) {
                dbClient.createConnection();
                String sql = String.format(SqlStatements.INSERT_NEW_IMAGE_TO_ALBUM,CTimage.getAlbumName());
                Object[] values = {"",req_body.getImage().getImageName(), req_body.getImage().getImageSize(),
                        req_body.getImage().getImageData(), req_body.getImage().getThumbnail(), CTimage.getUserName(),CTimage.getImageLength(), CTimage.getImageWidth()};
                boolean res = dbClient.dynamicPrepareStatement(sql,values);
                dbClient.closeConnection();
                returnMessage.setBody(fromClassToXml(responseBody));
            }
        } catch (SQLException | ClassNotFoundException e){
            returnMessage.getHeader().setCommandSuccess(false);
            logger.warn("Could not add image ", e);
            return returnMessage;
        }
        return returnMessage;
    }

    public boolean alreadyExist(String album_name, String image_name) throws SQLException, ClassNotFoundException{
        logger.debug("Checking if image " + image_name + " exists in album " +album_name);
        dbClient.createConnection();
        logger.debug("Connection to db was created");
        String[] is_image_exists = {"", image_name};
        ResultSet resultSet = dbClient.prepareStatementAllStrings(String.format(SqlStatements.SELECT_IMAGE_FROM_ALBUM, album_name)
                , is_image_exists);
        boolean has_image = resultSet.next(); //Checks we have no photos
        logger.info("Image" + " exists result=" + has_image);
        resultSet.close();
        dbClient.closeConnection();
        return has_image;
    }

}

