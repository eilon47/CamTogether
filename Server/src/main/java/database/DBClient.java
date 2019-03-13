package database;


import handlers.AddNewUserToAlbumHandler;
import org.postgresql.jdbc.PgResultSet;
import org.postgresql.largeobject.LargeObjectManager;
import xmls.CTImage;
import xmls.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;

public class DBClient {
    private Connection conn;
    private Statement statement;
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
        this.statement.close();
        this.conn.close();
    }

    //######################  Tables Creations ############################

    public boolean createTableFromString(String sql) throws SQLException{
         this.statement = this.conn.createStatement();
         return this.statement.execute(sql);
    }


    //##################### SELECT ###################################

    public ResultSet selectQuery(String selection, String table, String cond)throws SQLException{
        String sql = "SELECT " + selection + " FROM " + table;
        if (cond != null){
            sql += (" WHERE " + cond + ";");
        }

        this.statement = this.conn.createStatement();
        ResultSet rs = this.statement.executeQuery(sql);
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
        this.statement = conn.prepareStatement(sql);
        ((PreparedStatement)this.statement).setString(1, imageName);
        System.out.println(((PreparedStatement)this.statement));
        ResultSet rs = ((PreparedStatement)this.statement).executeQuery();
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
            return imageRecord;
        }
        return null;
    }


    public boolean insertImageRecord(CTImage imageRecord) throws SQLException, IOException {
        // All LargeObject API calls must be within a transaction block
        conn.setAutoCommit(false);

        // Now insert the row into imageslo
        this.statement = conn.prepareStatement(String.format(SqlStatements.INSERT_NEW_IMAGE_TO_ALBUM,
                imageRecord.getAlbumName()));
        ((PreparedStatement)this.statement).setString(1, imageRecord.getImageName());
        ((PreparedStatement)this.statement).setInt(2, imageRecord.getImageSize());
        ByteArrayInputStream is = new ByteArrayInputStream(imageRecord.getImageData());
        ((PreparedStatement)this.statement).setBinaryStream(3, is, imageRecord.getImageSize());
        ((PreparedStatement)this.statement).setString(4, imageRecord.getUserName());
        ((PreparedStatement)this.statement).setInt(5, imageRecord.getImageLength());
        ((PreparedStatement)this.statement).setInt(6, imageRecord.getImageWidth());
        System.out.println(((PreparedStatement)this.statement));
        ((PreparedStatement)this.statement).executeUpdate();
        this.conn.commit();
        return true;

    }

    // ####################### GENERAL ################################

    public ResultSet doSqlStatement(String sql) throws SQLException{
        this.statement = this.conn.createStatement();
        return this.statement.executeQuery(sql);
    }


    public ResultSet prepareStatementAllStrings(String sql, String[] values) throws SQLException{
        if (values.length == 0)
            return doSqlStatement(sql);
        statement = this.conn.prepareStatement(sql);
        for(int i=1; i < values.length; i++){
            ((PreparedStatement)this.statement).setString(i, values[i]);
        }
        return ((PreparedStatement)this.statement).executeQuery();
    }

    public boolean insertNewRecord(String sql, Object[] args) throws SQLException{
        if (args.length == 0)
            return false;
        statement = this.conn.prepareStatement(sql);
        for(int i=1; i < args.length; i++){
            if (args[i] == null)
                ((PreparedStatement)this.statement).setNull(i, Types.NULL);
            else if (args[i] instanceof String)
                ((PreparedStatement)this.statement).setString(i, (String) args[i]);
            else if (args[i] instanceof Integer)
                ((PreparedStatement)this.statement).setInt(i,(Integer) args[i]);
            else if (args[i] instanceof Boolean)
                ((PreparedStatement)this.statement).setBoolean(i,(Boolean) args[i]);
            else if (args[i] instanceof Long)
                ((PreparedStatement)this.statement).setLong(i,(Long) args[i]);
            else if (args[i] instanceof Float)
                ((PreparedStatement)this.statement).setFloat(i,(Float) args[i]);
            else if (args[i].getClass().equals( byte[].class) )
                ((PreparedStatement)this.statement).setBytes(i,(byte[]) args[i]);
            else
                throw new SQLException("Not implemented");
        }
        ((PreparedStatement)this.statement).executeUpdate();
        return true;
    }


    public boolean dynamicPrepareStatement(String sql, Object[] args) throws SQLException {
        statement = this.conn.prepareStatement(sql);
        for(int i = 1; i <= args.length -1; i++){
            if (args[i] instanceof String)
                ((PreparedStatement)this.statement).setString(i, (String) args[i]);
            else if (args[i] == null)
                ((PreparedStatement)this.statement).setNull(i, Types.NULL);
            else if (args[i] instanceof Integer)
                ((PreparedStatement)this.statement).setInt(i,(Integer) args[i]);
            else if (args[i] instanceof Long)
                ((PreparedStatement)this.statement).setLong(i,(Long) args[i]);
            else if (args[i] instanceof Float)
                ((PreparedStatement)this.statement).setFloat(i,(Float) args[i]);
            else if (args[i] instanceof Boolean)
                ((PreparedStatement)this.statement).setBoolean(i,(Boolean) args[i]);
            else if (args[i].getClass().equals( byte[].class) )
                ((PreparedStatement)this.statement).setBytes(i,(byte[]) args[i]);
            else
                throw new SQLException("Not implemented");
        }
        ((PreparedStatement)this.statement).executeUpdate();
        return true;
    }

    public boolean updateQuery(String update) throws SQLException{
        this.statement = this.conn.createStatement();
        //Execute method returns false if its doing update query
        boolean res = this.statement.execute(update);
        return !res;
    }

    public static void main(String [] a){
        DBClient dbClient = new DBClient();
        AddNewUserToAlbumHandler handler = new AddNewUserToAlbumHandler();
//        PgResultSet set1, set2;
        try {
//            dbClient.createConnection();
//            String[] values = {
//                    "", "id", "eilon", "yes", "eilonalbum", "info"
//            };
//            dbClient.insertNewRecord(SqlStatements.INSERT_NEW_USER_TO_USERS_TABLE, values);
            User user = new User();
            user.setUserID("id");
            user.setUserName("eilon");
            String new_val = handler.createNewParticipantsValue(user, "id-1");

//            boolean res = handler.checkUserExistsAndUnique(user);
//            System.out.println(res);
//            dbClient.closeConnection();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}



//TODO
//https://stackoverflow.com/questions/15127100/store-and-retrieve-images-in-postgresql-using-java