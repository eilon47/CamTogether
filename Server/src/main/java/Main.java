import database.DBClient;
import database.SqlStatements;
import handlers.MessageHandler;
import handlers.NewPhotoCommandHandler;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import xmls.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static Logger logger = LogManager.getLogger();
    public static void main(String args[]) {
//        try {
//            init_tables();
//        } catch (SQLException | ClassNotFoundException ex)
//        {
//            ex.printStackTrace();
//        }
        MessageHandler mh = new MessageHandler();
        HeaderRequest h_r = new HeaderRequest();
        NewImageRequestBody b_r = new NewImageRequestBody();

        h_r.setUserId("danielG");
        h_r.setCommand(CommandsEnum.ADD_NEW_PHOTO);

        b_r.setAlbum("testingAlbum");
        b_r.setImage(create_CTimage("/home/dandan/IdeaProjects/CamTogether/Server/src/main/resources/faces.jpeg"));

        RequestMessage rm = new RequestMessage();
        rm.setHeader(h_r);
        rm.setBody(mh.fromClassToXml(b_r));

        NewPhotoCommandHandler npc = new NewPhotoCommandHandler();
        npc.handle(rm);

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
            img.setImageName(imgg.toString());
            img.setImageData(imageInByte);
            img.setAlbumName("testingAlbum");
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
