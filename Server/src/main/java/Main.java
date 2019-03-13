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

        h_r.setUserId("dandan");
        h_r.setCommand(CommandsEnum.ADD_NEW_PHOTO);

        b_r.setAlbum("ct");
        b_r.setImage(create_CTimage("/home/dandan/IdeaProjects/CamTogether/Server/src/main/resources/faces.jpeg"));

        RequestMessage rm = new RequestMessage();
        rm.setHeader(h_r);
        rm.setBody(mh.fromClassToXml(b_r));

        NewPhotoCommandHandler npc = new NewPhotoCommandHandler();
        npc.handle(rm);

        HeaderRequest h_r_2 = new HeaderRequest();
        GetAlbumRequestBody b_r_2 = new GetAlbumRequestBody();

        h_r_2.setUserId("beni");
        h_r_2.setCommand(CommandsEnum.GET_ALBUM);

        b_r_2.setAlbumName("ct");

        RequestMessage rm_2 = new RequestMessage();
        rm_2.setHeader(h_r_2);
        rm_2.setBody(mh.fromClassToXml(b_r_2));

        GetAlbumHandler gah = new GetAlbumHandler();
        gah.handle(rm_2);

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
