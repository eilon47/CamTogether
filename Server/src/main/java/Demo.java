import common.ResourcesHandler;
import handlers.CreateNewAlbumCommandHandler;
import handlers.GetAlbumHandler;
import handlers.NewPhotoCommandHandler;
import xmls.*;
import xsd.XsdUtils;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Demo {
    public static void createNewAlbum(String album, String user){
        RequestMessage requestMessage = new RequestMessage();
        NewAlbumRequestBody requestBody = new NewAlbumRequestBody();
        HeaderRequest headerRequest = new HeaderRequest();

        requestBody.setAlbumName(album);
        requestBody.setManager(user);
        requestBody.setRules(new Rules());

        headerRequest.setCommand(CommandsEnum.CREATE_NEW_ALBUM);
        headerRequest.setUserId(user);

        requestMessage.setHeader(headerRequest);
        try {
            requestMessage.setBody(XsdUtils.serializeToXML(requestBody));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        CreateNewAlbumCommandHandler handler = new CreateNewAlbumCommandHandler();
        ResponseMessage responseMessage = handler.handle(requestMessage);
        try {
            System.out.println(XsdUtils.serializeToXML(responseMessage));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static void addNewPhoto(String album, String user, String img){
        RequestMessage requestMessage = new RequestMessage();
        NewImageRequestBody requestBody = new NewImageRequestBody();
        HeaderRequest headerRequest = new HeaderRequest();

        requestBody.setImage(create_CTimage(ResourcesHandler.getResourceFilePath("faces.jpeg"), album, user));
        requestBody.setAlbum(album);

        headerRequest.setCommand(CommandsEnum.ADD_NEW_PHOTO);
        headerRequest.setUserId(user);
        requestMessage.setHeader(headerRequest);
        try {
            requestMessage.setBody(XsdUtils.serializeToXML(requestBody));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        NewPhotoCommandHandler handler = new NewPhotoCommandHandler();
        ResponseMessage responseMessage = handler.handle(requestMessage);
        try {
            System.out.println(XsdUtils.serializeToXML(responseMessage));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    private static CTImage create_CTimage(String path, String album, String user) {
        try {
            CTImage img = new CTImage();
            File f = new File(path);
            BufferedImage imgg = ImageIO.read(f);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( imgg, "jpg", baos );
            baos.flush();
            byte[] imageInByte = baos.toByteArray();

            img.setImageSize(imgg.getWidth()*imgg.getHeight());
            img.setImageName(f.getName());
            img.setImageData(imageInByte);
            img.setAlbumName(album);
            img.setUserName(common.IdGen.generate(imgg.toString()));
            img.setImageLength(imgg.getHeight());
            img.setImageWidth(imgg.getWidth());
            img.setUserName(user);
            return img;

        }catch (IOException e) {
            return null;
        }
    }

    static public CTImage getAlbum(String album, String user){
        RequestMessage requestMessage = new RequestMessage();
        GetAlbumRequestBody requestBody = new GetAlbumRequestBody();
        HeaderRequest headerRequest = new HeaderRequest();

        requestBody.setAlbumName(album);

        headerRequest.setCommand(CommandsEnum.GET_ALBUM);
        headerRequest.setUserId(user);
        requestMessage.setHeader(headerRequest);
        try {
            requestMessage.setBody(XsdUtils.serializeToXML(requestBody));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        GetAlbumHandler handler = new GetAlbumHandler();
        ResponseMessage responseMessage = handler.handle(requestMessage);
        try {
            GetAlbumResponseBody responseBody = XsdUtils.serializeFromXml(responseMessage.getBody(), GetAlbumResponseBody.class);
            return responseBody.getImages().get(0);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public void main(String[] args){
        String album = "demoAlbum";
       // createNewAlbum(album, "demoUser");
        addNewPhoto(album, "demoUser", "faces.jpeg");
        CTImage img = getAlbum(album, "demoUser");
        System.out.println(img);
    }
}
