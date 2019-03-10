package database;



import common.ResourcesHandler;
import org.postgresql.largeobject.LargeObjectManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
    private static final String username = "danielG";
    private static final String password = "1234";
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/camTogether";


    //####################### Connections ################################

    public void createConnection() throws ClassNotFoundException, SQLException{
        Class.forName(JDBC_DRIVER);
        this.conn = DriverManager.getConnection(DB_URL, username, password);
        System.out.println("connected!");
    }

    public void closeConnection() throws SQLException{
        this.conn.close();
    }

    //######################  Tables Creations ############################

    public ResultSet createTableFromString(String sql) throws SQLException{
        PreparedStatement statement = this.conn.prepareStatement(sql);
        ResultSet set = statement.executeQuery();
        statement.close();
        return set;
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

    public ResultSet doSqlStatement(String sql) throws SQLException{
        Statement statement = this.conn.createStatement();
        ResultSet set = statement.executeQuery(sql);
        statement.close();
        return set;
    }

    public ResultSet prepareStatementAllStrings(String sql, String[] values) throws SQLException{
        if (values.length == 0)
            return doSqlStatement(sql);
        PreparedStatement statement = this.conn.prepareStatement(sql);
        for(int i=0; i < values.length; i++){
            statement.setString(i, values[i]);
        }
        ResultSet set = statement.executeQuery(sql);
        statement.close();
        return set;
    }

    public ResultSet dynamicPrepareStatement(String sql, Object[] args) throws SQLException{
        PreparedStatement statement = this.conn.prepareStatement(sql);
        for(int i =1; i <= args.length; i++){
            if (args[i] instanceof String)
                statement.setString(i, (String) args[i]);
            else if (args[i] instanceof Integer)
                statement.setInt(i,(Integer) args[i]);
            else if (args[i] instanceof Long)
                statement.setLong(i,(Long) args[i]);
            else if (args[i] instanceof Float)
                statement.setFloat(i,(Float) args[i]);
            else if (args[i] instanceof Boolean)
                statement.setBoolean(i,(Boolean) args[i]);
            else if (args[i] instanceof byte[])
                statement.setBytes(i,(byte[]) args[i]);
            else
                throw new NotImplementedException();
        }
        ResultSet set = statement.executeQuery(sql);
        statement.close();
        return set;
    }

}

//TODO
//https://stackoverflow.com/questions/15127100/store-and-retrieve-images-in-postgresql-using-java