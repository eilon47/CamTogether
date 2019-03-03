package database;



import common.ResourcesHandler;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.Arrays;

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

    public boolean createTable(String tableName, String primaryKey, String... keys) throws SQLException{
//        this.statement = getStatement();
//        StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (" + primaryKey);
//        for(String key :keys){
//            sql.append(", " + key);
//        }
//        sql.append(");");
//        System.out.println(sql);
//        statement.execute(sql.toString());
//        statement.close();
        return true;
    }

    public boolean createTableFromString(String sql) throws SQLException{
        PreparedStatement statement = this.conn.prepareStatement(sql);
        statement.executeQuery();
        statement.close();
        return true;
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
        String sql = String.format(SqlStatements.SELECT_IMAGE_FROM_TABLE, table);
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
                byte buf[] = new byte[oid];
                rs.getAsciiStream("image").read(buf, 0, oid);
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

    // #################### UPDATE #################################

    public boolean updateOp(String table, String key, String value, String cond)throws SQLException, ClassNotFoundException{
        String sql = "UPDATE " + table + " SET " + key + "=" +value + " WHERE " + cond;
        return doOperation(sql);
    }

    // ####################### INSERT ################################

    public boolean insertOneRecord(String table, String ... values) throws SQLException, ClassNotFoundException {
        return insertManyRecords(table, 1, values);
    }

    public boolean insertManyRecords(String table, int numOfRecords ,String ... values) throws SQLException, ClassNotFoundException{
        String sql = "INSERT INTO "+ table +" VALUES ";
        int vals_in_rec = values.length/numOfRecords;
        String vals[] = new String[numOfRecords];
        for(int i = 0; i < numOfRecords; i++){
            vals[i] = "(" + String.join(",", Arrays.copyOfRange(values, i*vals_in_rec, i*vals_in_rec + vals_in_rec)) + ")";
        }
        sql += String.join(", ", vals) + ";";
        return doOperation(sql);
    }

    public boolean insertToSpecificKeysOp(String table, String keys[], String... values) throws SQLException, ClassNotFoundException{
        int len_keys = keys.length;
        int len_values = values.length;
        if (len_values % len_keys != 0)
            return false;
        int records = len_values/len_keys;
        String skeys ="(" + String.join(", ", keys) + ")";
        String svalues[] = new String[records];
        for(int i =0; i<records; i++){
            svalues[i] = "(" + String.join(", ", Arrays.copyOfRange(values, i*len_keys, i*(len_keys+1))) + ")";
        }
        String sql = "INSERT INTO " + table + " " + skeys + " VALUES " + String.join(", ", svalues) + ";";
        return doOperation(sql);
    }

    public boolean insertImageRecord(CTImageRecord imageRecord) throws SQLException, IOException {
        // All LargeObject API calls must be within a transaction block
        conn.setAutoCommit(false);

        // Now insert the row into imageslo
        PreparedStatement ps = conn.prepareStatement(String.format(SqlStatements.INSERT_NEW_IMAGE, imageRecord.getAlbum()));
        ps.setString(1, imageRecord.getName());
        ps.setBinaryStream(2, imageRecord.getData(), imageRecord.getSize());
        ps.setString(3, imageRecord.getUser_id());
        System.out.println(ps);
        ps.executeUpdate();
        ps.close();
        return true;

    }

    public void insetTest() throws Exception{
        File file = new File(ResourcesHandler.getResourceFilePath("test_image.jpg"));
        FileInputStream fis = new FileInputStream(file);
        PreparedStatement ps = conn.prepareStatement("INSERT INTO album_test2 VALUES (?, ?, ?, ?)");
        ps.setString(1, file.getName());
        ps.setInt(2, (int) file.length());
        ps.setBinaryStream(3, fis, (int)file.length());
        ps.setString(4,"eilon47");
        ps.executeUpdate();
        ps.close();
        fis.close();
    }

    // ####################### DELETE ################################

    public boolean deleteOp(String table, String cond) throws SQLException, ClassNotFoundException{
        String sql = "DELETE FROM " + table + " WHERE " + cond;
        return doOperation(sql);
    }

    // ####################### GENERAL ################################

    private boolean doOperation(String sql) throws SQLException, ClassNotFoundException{
//        this.statement = getStatement();
//        this.conn.setAutoCommit(false);
//        this.statement.execute(sql);
//        this.conn.commit();
        return true;
    }



    public static void main(String[] args) {
        try {
            String album = "album_test2";
            String user_id = "eilon_test";
            DBClient client = new DBClient();
            File f = new File(ResourcesHandler.getResourceFilePath("test_image.jpg"));
           // byte[] img = Files.readAllBytes(f.toPath());
            client.createConnection();
            //client.createTableFromString(String.format(SqlStatements.NEW_ALBUM_ST, album));
            //client.insertImageRecord(new CTImageRecord(f.getName(), img, user_id, album));
            //client.insetTest();
            CTImageRecord imgRec = client.selectQueryForImg(album, f.getName());


                byte[] buf = new byte[imgRec.size];
                imgRec.getData().read(buf, 0, buf.length);;
               BufferedImage img = ImageIO.read(new ByteArrayInputStream(buf));
            File outputfile = new File("image.jpg");
            ImageIO.write(img, "jpg", outputfile)  ;

                //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream

            client.closeConnection();


        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

//TODO
//https://stackoverflow.com/questions/15127100/store-and-retrieve-images-in-postgresql-using-java