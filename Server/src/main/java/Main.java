import common.ResourcesHandler;
import database.DBClient;
import database.SqlStatements;
import handlers.CreateNewAlbumCommandHandler;
import handlers.GetAlbumHandler;
import handlers.MessageHandler;
import handlers.NewPhotoCommandHandler;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import xmls.*;
import xsd.XsdUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static Logger logger = LogManager.getLogger();
    public static void main(String args[]) throws JAXBException {
//        try {
//            init_tables();
//        } catch (SQLException | ClassNotFoundException ex)
//        {
//            ex.printStackTrace();
//        }

//        NewAlbumRequestBody r1 = new NewAlbumRequestBody();
//        RequestMessage r2 = new RequestMessage();
//        HeaderRequest r3 = new HeaderRequest();
//        r3.setCommand(CommandsEnum.CREATE_NEW_ALBUM);
//        r3.setUserId("dandan");
//        r1.setAlbumName("test1");
//        r1.setManager("dandan");
//        r2.setHeader(r3);
//        r1.setRules(new Rules());
//        r2.setBody(XsdUtils.serializeToXML(r1));
//
//        CreateNewAlbumCommandHandler handler=new CreateNewAlbumCommandHandler();
//        handler.handle(r2);

        MessageHandler mh = new MessageHandler();
        HeaderRequest h_r = new HeaderRequest();

        NewImageRequestBody b_r = new NewImageRequestBody();

        h_r.setUserId("dandan");
        h_r.setCommand(CommandsEnum.ADD_NEW_PHOTO);

        b_r.setAlbum("test1");
        b_r.setImage(create_CTimage(ResourcesHandler.getResourceFilePath("faces2.jpg")));
        RequestMessage rm = new RequestMessage();
        rm.setHeader(h_r);
        rm.setBody(mh.fromClassToXml(b_r));

        NewPhotoCommandHandler npc = new NewPhotoCommandHandler();
        npc.handle(rm);

        HeaderRequest h_r_2 = new HeaderRequest();
        GetAlbumRequestBody b_r_2 = new GetAlbumRequestBody();

        h_r_2.setUserId("beni");
        h_r_2.setCommand(CommandsEnum.GET_ALBUM);

        b_r_2.setAlbumName("test1");

        RequestMessage rm_2 = new RequestMessage();
        rm_2.setHeader(h_r_2);
        rm_2.setBody(mh.fromClassToXml(b_r_2));

        GetAlbumHandler gah = new GetAlbumHandler();
        ResponseMessage photos = gah.handle(rm_2);

        GetAlbumResponseBody responseBody = XsdUtils.serializeFromXml(photos.getBody(),GetAlbumResponseBody.class);
        List<CTImage> f = responseBody.getImages();

    }
    private static void init_tables()throws SQLException, ClassNotFoundException {
        DBClient client = new DBClient();
        client.createConnection();

        for(String table : SqlStatements.INIT_BASIC_TABLES){
           boolean res = client.createTableFromString(table);
        }
        client.closeConnection();
    }
    private static CTImage create_CTimage(String path) {
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
            img.setAlbumName("test1");
            img.setUserName(common.IdGen.generate(imgg.toString()));
            img.setImageLength(imgg.getHeight());
            img.setImageWidth(imgg.getWidth());
            img.setUserName("dandan");
            return img;

        }catch (IOException e) {
            return null;
        }
    }
}
