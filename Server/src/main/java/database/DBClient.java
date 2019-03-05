package database;



import common.ResourcesHandler;
import org.postgresql.largeobject.LargeObjectManager;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStreamImpl;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Map;

public class DBClient {
    private Connection conn;
    private static final String username = "picturex";
    private static final String password = "1234";
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/basedb";


    //####################### Connections ################################

    public void createConnection() throws ClassNotFoundException, SQLException{
        Class.forName(JDBC_DRIVER);
        this.conn = DriverManager.getConnection(DB_URL, username, password);
    }

    public void closeConnection() throws SQLException{
        this.conn.close();
    }

    //######################  Tables Creations ############################

    public void createTableFromString(String sql) throws SQLException{
        PreparedStatement statement = this.conn.prepareStatement(sql);
        statement.executeQuery();
        statement.close();
    }


    //##################### SELECT ###################################

    public ResultSet selectQuery(String selection, String table, String cond)throws SQLException{
        String sql = "SELECT " + selection + " FROM " + table;
        if (cond != null){
            sql += (" WHERE " + cond);
        }

        Statement statement = this.conn.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        return rs;
    }

    public ResultSet selectQuery(String selection, String table)throws SQLException {
        return selectQuery(selection, table, null);
    }

    public CTImageRecord selectQueryForImg(String table, String imageName) throws SQLException, IOException{
        // All LargeObject API calls must be within a transaction block
        conn.setAutoCommit(false);
        // Get the Large Object Manager to perform operations with
        LargeObjectManager lobj = ((org.postgresql.PGConnection)conn).getLargeObjectAPI();
        String sql = String.format(SqlStatements.SELECT_IMAGE_FROM_ALBUM, table);
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, imageName);
        System.out.println(ps);
        ResultSet rs = ps.executeQuery();

        if (rs != null) {
            CTImageRecord imageRecord = new CTImageRecord();
            imageRecord.setAlbum(table);

            //Gets only the first image.
            while (rs.next()) {
                // Open the large object for reading
                int oid = rs.getInt("image_size");
                byte buf[] = rs.getBytes("image");
                imageRecord.setName(rs.getString("image_name"));
                imageRecord.setUser_id(rs.getString("user_id"));
                imageRecord.setData(buf);
                // Close the object
            }
            rs.close();
            ps.close();
            return imageRecord;
        }
        ps.close();
        return null;
    }


    public boolean insertImageRecord(CTImageRecord imageRecord) throws SQLException, IOException {
        // All LargeObject API calls must be within a transaction block
        conn.setAutoCommit(false);

        // Now insert the row into imageslo
        PreparedStatement ps = conn.prepareStatement(String.format(SqlStatements.INSERT_NEW_IMAGE_TO_ALBUM,
                imageRecord.getAlbum()));
        ps.setString(1, imageRecord.getName());
        ps.setInt(2, imageRecord.getDataSize());
        ByteArrayInputStream is = new ByteArrayInputStream(imageRecord.getData());
        ps.setBinaryStream(3, is, imageRecord.getDataSize());
        ps.setString(4, imageRecord.getUser_id());
        ps.setInt(5, imageRecord.getLength());
        ps.setInt(6, imageRecord.getWidth());
        System.out.println(ps);
        ps.executeUpdate();
        ps.close();
        return true;

    }

    // ####################### GENERAL ################################

    public ResultSet doStatement(String sql) throws SQLException{
        Statement statement = this.conn.createStatement();
        ResultSet set = statement.executeQuery(sql);
        statement.close();
        return set;
    }

    public ResultSet prepareStatementAllStrings(String sql, String... values) throws SQLException{
        if (values.length == 0)
            return doStatement(sql);
        PreparedStatement statement = this.conn.prepareStatement(sql);
        for(int i=0; i < values.length; i++){
            statement.setString(i, values[i]);
        }
        ResultSet set = statement.executeQuery(sql);
        statement.close();
        return set;
    }


    public static void main(String[] args) {
        try {
            String album = "album_test2";
            String user_id = "eilon_test";
            DBClient client = new DBClient();
            client.createConnection();
            File f = new File(ResourcesHandler.getResourceFilePath("test_image.jpg"));
            CTImageRecord ir = client.selectQueryForImg(album, f.getName());
            BufferedImage img = ImageIO.read(f);
            ByteArrayInputStream bais = new ByteArrayInputStream(ir.getData());
            BufferedImage img2 = ImageIO.read(ImageIO.createImageInputStream(bais));
            ImageIcon icon=new ImageIcon(img2);
            JFrame frame=new JFrame();
            frame.setLayout(new FlowLayout());
            frame.setSize(200,300);
            JLabel lbl=new JLabel();
            lbl.setIcon(icon);
            frame.add(lbl);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.closeConnection();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

//TODO
//https://stackoverflow.com/questions/15127100/store-and-retrieve-images-in-postgresql-using-java