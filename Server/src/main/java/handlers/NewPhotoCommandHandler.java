package handlers;

import client.CVClient;
import database.SqlStatements;
import xmls.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
            CTImage img = req_body.getImage();
            boolean cv_res = this.cvClient.queryCvServer(img);
            if(!cv_res){
                logger.info("CV server returned false - not inserting image to db");
                returnMessage.getHeader().setCommandSuccess(false);
                returnMessage.setBody(fromClassToXml(responseBody));
                return returnMessage;
            }
            if (!alreadyExist(img.getAlbumName(),img.getImageName())) {
                dbClient.createConnection();
                String insert_image_sql = String.format(SqlStatements.INSERT_NEW_IMAGE_TO_ALBUM,img.getAlbumName());
                Object[] values = {"", img.getImageName(), img.getImageSize(), img.getImageData(), img.getTitle(), img.getImageHeight(),
                        img.getImageWidth(), img.getUserName(), img.getDate(), img.getLongitude(), img.getLatitude(), img.getLatitude(), img.getAlbumName()};
                boolean res = dbClient.dynamicQuery(insert_image_sql,values);
                CTThumbnail thumbnail = createThumbnail(img, 100, 100);
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

    private CTThumbnail createThumbnail(CTImage img, int height, int width){
        CTThumbnail thumbnail = new CTThumbnail();
        thumbnail.setThumbnailName(img.getImageName());
        thumbnail.setThumbnailHeight(height);
        thumbnail.setThumbnailWidth(width);
        ByteArrayInputStream bis = new ByteArrayInputStream(img.getImageData());
        try {
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            bufferedImage.createGraphics().drawImage(ImageIO.read(bis).getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,0, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos );
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            thumbnail.setThumbnailData(imageInByte);
        } catch (IOException e) {
            logger.warn("failed to create thumbnail image from " + img.getImageName());
            //thumbnail.setThumbnailData(); todo add default thumbnail photo
        }
        return thumbnail;
    }
}

