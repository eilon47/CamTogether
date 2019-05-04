import database.DBClient;
import database.SqlStatements;
import handlers.CreateNewAlbumCommandHandler;
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
        try {
            init_tables();
           // initData();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
//        Server server = new Server("0.0.0.0", 23456);
//        try {
//            server.connect();
//            server.startServer();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    private static void init_tables()throws SQLException, ClassNotFoundException {
        DBClient client = new DBClient();
        client.createConnection();

        for(String table : SqlStatements.INIT_BASIC_TABLES){
           boolean res = client.createTableFromString(table);
        }
        client.closeConnection();
    }


    private static void createAlbum(String name){
        CreateNewAlbumCommandHandler handler = new CreateNewAlbumCommandHandler();

    }

    private static void initData() throws SQLException, ClassNotFoundException {
        DBClient client = new DBClient();
        client.createConnection();
        String[] user1 = {"", "user1", "user1", "", "", ""};
        String[] user2 = {"", "user2", "user2", "", "", ""};
        client.insertQuery(SqlStatements.INSERT_NEW_USER_TO_USERS_TABLE, user1);
        client.insertQuery(SqlStatements.INSERT_NEW_USER_TO_USERS_TABLE, user2);
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
            // img.setAlbumName("testingAlbum");
            img.setUserName(common.IdGen.generate(imgg.toString()));
            img.setImageHeight(imgg.getHeight());
            img.setImageWidth(imgg.getWidth());
            img.setUserName("dandan");
            return img;

        }catch (IOException e) {
            return null;
        }
    }

    private static void drop() throws SQLException, ClassNotFoundException {
        DBClient client = new DBClient();
        client.createConnection();
        client.insertQuery("drop table albums, rules, users;");
        client.closeConnection();
    }



}
