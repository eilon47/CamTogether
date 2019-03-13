package database;


import org.postgresql.largeobject.LargeObjectManager;
import xmls.CTImage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;

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
        System.out.println("connected!");
    }

    public void closeConnection() throws SQLException{
        this.conn.close();
    }

    //######################  Tables Creations ############################

    public boolean createTableFromString(String sql) throws SQLException{
        Statement statement = this.conn.createStatement();
        boolean res = statement.execute(sql);

        statement.close();
        return res;
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

    public CTImage selectQueryForImg(String table, String imageName) throws SQLException, IOException{
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
            CTImage imageRecord = new CTImage();
            imageRecord.setAlbumName(table);

            //Gets only the first image.
            while (rs.next()) {
                // Open the large object for reading
                int oid = rs.getInt("image_size");
                byte buf[] = rs.getBytes("image");
                imageRecord.setImageName(rs.getString("image_name"));
                imageRecord.setUserName(rs.getString("user_id"));
                imageRecord.setImageData(buf);
                // Close the object
            }
            rs.close();
            ps.close();
            return imageRecord;
        }
        ps.close();
        return null;
    }


    public boolean insertImageRecord(CTImage imageRecord) throws SQLException, IOException {
        // All LargeObject API calls must be within a transaction block
        conn.setAutoCommit(false);

        // Now insert the row into imageslo
        PreparedStatement ps = conn.prepareStatement(String.format(SqlStatements.INSERT_NEW_IMAGE_TO_ALBUM,
                imageRecord.getAlbumName()));
        ps.setString(1, imageRecord.getImageName());
        ps.setInt(2, imageRecord.getImageSize());
        ByteArrayInputStream is = new ByteArrayInputStream(imageRecord.getImageData());
        ps.setBinaryStream(3, is, imageRecord.getImageSize());
        ps.setString(4, imageRecord.getUserName());
        ps.setInt(5, imageRecord.getImageLength());
        ps.setInt(6, imageRecord.getImageWidth());
        System.out.println(ps);
        ps.executeUpdate();
        ps.close();
        return true;

    }

    // ####################### GENERAL ################################

    public ResultSet doSqlStatement(String sql) throws SQLException{
        Statement statement = this.conn.createStatement();
        ResultSet set = statement.executeQuery(sql);
        //statement.close();
        return set;
    }


    public ResultSet prepareStatementAllStrings(String sql, String[] values) throws SQLException{
        if (values.length == 0)
            return doSqlStatement(sql);
        PreparedStatement statement = this.conn.prepareStatement(sql);
        for(int i=1; i < values.length; i++){
            statement.setString(i, values[i]);
        }
        ResultSet set = statement.executeQuery();
        statement.close();
        return set;
    }

    public boolean insertNewRecord(String sql, Object[] args) throws SQLException{
        if (args.length == 0)
            return false;
        PreparedStatement statement = this.conn.prepareStatement(sql);
        for(int i=1; i < args.length; i++){
            if (args[i] == null)
                statement.setNull(i, Types.NULL);
            else if (args[i] instanceof String)
                statement.setString(i, (String) args[i]);
            else if (args[i] instanceof Integer)
                statement.setInt(i,(Integer) args[i]);
            else if (args[i] instanceof Boolean)
                statement.setBoolean(i,(Boolean) args[i]);
            else if (args[i] instanceof Long)
                statement.setLong(i,(Long) args[i]);
            else if (args[i] instanceof Float)
                statement.setFloat(i,(Float) args[i]);
            else if (args[i].getClass().equals( byte[].class) )
                statement.setBytes(i,(byte[]) args[i]);
            else
                throw new SQLException("Not implemented");
        }
        statement.executeUpdate();
        statement.close();
        return true;
    }


    public boolean dynamicPrepareStatement(String sql, Object[] args) throws SQLException {
        PreparedStatement statement = this.conn.prepareStatement(sql);
        for(int i = 1; i <= args.length -1; i++){
            if (args[i] instanceof String)
                statement.setString(i, (String) args[i]);
            else if (args[i] == null)
                statement.setNull(i, Types.NULL);
            else if (args[i] instanceof Integer)
                statement.setInt(i,(Integer) args[i]);
            else if (args[i] instanceof Long)
                statement.setLong(i,(Long) args[i]);
            else if (args[i] instanceof Float)
                statement.setFloat(i,(Float) args[i]);
            else if (args[i] instanceof Boolean)
                statement.setBoolean(i,(Boolean) args[i]);
            else if (args[i].getClass().equals( byte[].class) )
                statement.setBytes(i,(byte[]) args[i]);
            else
                throw new SQLException("Not implemented");
        }
        statement.executeUpdate();
        statement.close();
        return true;
    }
    public boolean dynamicPrepareGETStatement(String sql, Object[] args) throws SQLException {
        PreparedStatement statement = this.conn.prepareStatement(sql);
        for(int i = 1; i <= args.length -1; i++){
            if (args[i] instanceof String)
                statement.setString(i, (String) args[i]);
            else if (args[i] == null)
                statement.setNull(i, Types.NULL);
            else if (args[i] instanceof Integer)
                statement.setInt(i,(Integer) args[i]);
            else if (args[i] instanceof Long)
                statement.setLong(i,(Long) args[i]);
            else if (args[i] instanceof Float)
                statement.setFloat(i,(Float) args[i]);
            else if (args[i] instanceof Boolean)
                statement.setBoolean(i,(Boolean) args[i]);
            else if (args[i].getClass().equals( byte[].class) )
                statement.setBytes(i,(byte[]) args[i]);
            else
                throw new SQLException("Not implemented");
        }
        statement.executeQuery();
        statement.close();
        return true;
    }

}

//TODO
//https://stackoverflow.com/questions/15127100/store-and-retrieve-images-in-postgresql-using-java