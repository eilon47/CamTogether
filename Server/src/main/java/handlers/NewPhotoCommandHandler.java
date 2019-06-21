package handlers;

import client.CVClient;
import common.ImageUtils;
import database.SqlStatements;
import xmls.*;

import javax.imageio.ImageIO;
import javax.xml.datatype.XMLGregorianCalendar;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class NewPhotoCommandHandler extends CommandHandler {
    private CVClient cvClient = new CVClient();
    @Override
    public ResponseMessage handle(RequestMessage request){
        logger.info("Handling new photo");
        ResponseMessage returnMessage = new ResponseMessage();
        //create header of message
        returnMessage.setHeader(createResponseHeader(request.getHeader()));
        NewImageRequestBody req_body = fromXmlToClass(request.getBody(), NewImageRequestBody.class);
        req_body.setAlbum(req_body.getAlbum().replace(" ", "_"));
        CTImage ct = req_body.getImage();
        ct.setAlbumName(ct.getAlbumName().replace(" ","_"));
        req_body.setImage(ct);
        NewImageResponseBody responseBody = new NewImageResponseBody();
        responseBody.setAlbum(req_body.getAlbum());
        responseBody.setImage(req_body.getImage().getImageName());
        try {
            CTImage img = req_body.getImage();
            if(!checkRulesForImage(img)){
                logger.info("Image " + img.getImageName() + " didn't pass the rules criteria of album " + img.getAlbumName());
                returnMessage.getHeader().setCommandSuccess(false);
                returnMessage.setBody(fromClassToXml(responseBody));
                return returnMessage;
            }
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
                        img.getImageWidth(), img.getUserName(), img.getDate(), img.getLongitude(), img.getLatitude(), img.getAlbumName()};
                boolean res = dbClient.insertQuery(insert_image_sql,values);
                CTThumbnail thumbnail = createThumbnail(img, ImageUtils.THUMBNAIL_IMG_SIZE, ImageUtils.THUMBNAIL_IMG_SIZE);
                String insert_thumb_sql = String.format(SqlStatements.INSERT_NEW_THUMBNAIL_TO_ALBUM, img.getAlbumName());
                Object[] values2 = {"", thumbnail.getThumbnailName(), thumbnail.getThumbnailHeight(), thumbnail.getThumbnailWidth(),thumbnail.getThumbnailData()};
                res = dbClient.insertQuery(insert_thumb_sql, values2);
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

    private boolean checkRulesForImage(CTImage image) throws SQLException, ClassNotFoundException {
        Rules rules = null;
        rules = dbClient.getAlbumRules(image.getAlbumName());
        if(rules == null)
            return true;
        boolean location = nullityCheck(rules.getRadius()) && nullityCheck(rules.getLatitude()) && nullityCheck(rules.getLongitude());
        boolean time = nullityCheck(rules.getEndTime()) && nullityCheck(rules.getStartTime());
//        if(time) {
//            SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
//            try {
//                Date start = dt.parse(rules.getStartTime());
//                Date end = dt.parse(rules.getEndTime());
//                Date imgDate = dt.parse(image.getDate());
//                if(imgDate.before(start) || imgDate.after(end))
//                    return false;
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        if(location){
//            float longitude = image.getLatitude();
//            float latitude = image.getLatitude();
//            if (latitude ==0 && longitude == 0)
//            {
//                logger.warn("Could not get longitue and latitude from image");
//                return true;
//            }
//            double distance = Math.sqrt(Math.pow(longitude - rules.getLongitude(), 2) + Math.pow(latitude - rules.getLatitude(), 2));
//            if(distance > rules.getRadius())
//                return false;
//        }
        return true;
    }

    public Date fromXMLDateToDate(XMLGregorianCalendar date) {
        return new Date( date.toGregorianCalendar().getTimeInMillis());
    }
    private boolean nullityCheck(Object o){
        return o != null;
    }

    private boolean alreadyExist(String album_name, String image_name) throws SQLException, ClassNotFoundException{
        logger.debug("Checking if image " + image_name + " exists in album " +album_name);
        dbClient.createConnection();
        logger.debug("Connection to db was created");
        String[] is_image_exists = {"", image_name};
        ResultSet resultSet = dbClient.selectQuery(String.format(SqlStatements.SELECT_IMAGE_FROM_ALBUM, album_name)
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
        try {
            thumbnail.setThumbnailData(getThumbBytes(img.getImageData(), width, height));
        } catch (IOException e){
            //TODO Create default thumbnail
        }
//        ByteArrayInputStream bis = new ByteArrayInputStream(img.getImageData());
        return thumbnail;
    }

    private byte[] getThumbBytes(byte[] image, int tThumbWidth, int tThumbHeight) throws IOException {
        ImageUtils imageUtils = new ImageUtils();
        return imageUtils.createThumbnail(image, tThumbHeight, tThumbWidth);
    }

    public static CTImage create_CTimage(String s) {
        try {
            File f = new File(s);
            CTImage img = new CTImage();
            BufferedImage imgg = ImageIO.read(f);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( imgg, "jpg", baos );
            baos.flush();
            byte[] imageInByte = baos.toByteArray();

            img.setImageSize(imgg.getWidth()*imgg.getHeight());
            img.setImageName(f.getName());
            img.setImageData(imageInByte);
            img.setAlbumName("sda");
            img.setUserName("dsadsa");
            return img;

        }catch (IOException e) {
            return null;
        }
    }
    public static void main(String [] args) {
//        CTImage ct = create_CTimage("C:\\Users\\eilon\\Desktop\\אילון\\cellphone\\WA\\20180419_130103.jpg");
//        CTThumbnail thumbnail =  createThumbnail(ct, 100, 100);

    }
}

