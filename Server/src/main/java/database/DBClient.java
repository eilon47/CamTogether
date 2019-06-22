package database;


import common.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xmls.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.GregorianCalendar;

public class DBClient {
    private Connection conn;
    private Statement statement;
    private static final String username = "picturex";
    private static final String password = "1234";
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/basedb";
    private static Logger logger = LogManager.getLogger("database");

    //####################### Connections ################################

    public void createConnection() throws ClassNotFoundException, SQLException{
        Class.forName(JDBC_DRIVER);
        this.conn = DriverManager.getConnection(DB_URL, username, password);
        logger.debug("connection created");
    }

    public void closeConnection() throws SQLException{
        this.statement.close();
        this.conn.close();
        logger.debug("connection and statement are closed");
    }

    //######################  Tables Creations ############################

    public boolean createTableFromString(String sql) throws SQLException{
         this.statement = this.conn.createStatement();
         logger.info("Executing Query " + sql);
         return this.statement.execute(sql);
    }

    public Rules getAlbumRules(String album) throws SQLException, ClassNotFoundException {
        Rules rules = null;
        this.createConnection();
        ResultSet rs = selectQuery(String.format(SqlStatements.SELECT_RULES_FOR_ALBUM, album));
        if(rs.next()) {
            rules = new Rules();
            boolean location = rs.getBoolean("location");
            boolean time = rs.getBoolean("time");
            rules.setRadius(rs.getInt("radius"));
            rules.setLatitude(rs.getFloat("latitude"));
            rules.setLongitude(rs.getFloat("longitude"));
            rules.setStartTime(rs.getString("start_date"));
            rules.setEndTime(rs.getString("end_date"));

        }
        rs.close();
        this.closeConnection();
        return rules;
    }

    public CTImage selectQueryForImg(String table, String imageName) throws SQLException, IOException{
        // All LargeObject API calls must be within a transaction block
        conn.setAutoCommit(false);
        // Get the Large Object Manager to perform operations with
        String sql = String.format(SqlStatements.SELECT_IMAGE_FROM_ALBUM, table);
        Object[] args = {"", imageName};
        ResultSet rs = selectQuery(sql, args);
        if (rs != null) {
            CTImage imageRecord = new CTImage();
            //Gets only the first image.
            while (rs.next()) {
                // Open the large object for reading
                byte buf[] = rs.getBytes("image");
                imageRecord.setImageName(rs.getString("image_name"));
                imageRecord.setImageData(buf);
                imageRecord.setImageSize(rs.getInt("image_size"));
                imageRecord.setTitle(rs.getString("title"));
                imageRecord.setImageHeight(rs.getInt("height"));
                imageRecord.setImageWidth(rs.getInt("width"));
                imageRecord.setUserName(rs.getString("username"));
                imageRecord.setUserName(rs.getString("user_id"));
                GregorianCalendar calendar = new GregorianCalendar();
                imageRecord.setDate(rs.getString("date"));
                imageRecord.setLongitude(rs.getFloat("longitude"));
                imageRecord.setLatitude(rs.getFloat("latitude"));
                // Close the object
            }
            rs.close();
            return imageRecord;
        }
        return null;
    }
    public boolean insertImageRecord(CTImage imageRecord, String album) throws SQLException, IOException {
        // All LargeObject API calls must be within a transaction block
        conn.setAutoCommit(false);
        CTThumbnail thumbnail = createThumbnail(imageRecord);
        String sql = String.format(SqlStatements.INSERT_NEW_IMAGE_TO_ALBUM, album);
        Object[] args = {"", imageRecord.getImageName(), imageRecord.getImageSize(), imageRecord.getImageData(),imageRecord.getTitle(),
                imageRecord.getImageHeight(), imageRecord.getImageWidth(), imageRecord.getUserName(), imageRecord.getDate(), imageRecord.getLongitude(),
                imageRecord.getLatitude() , imageRecord.getAlbumName()};
        boolean res = insertQuery(sql, args);
        logger.info("Result of inserting image " +imageRecord.getImageName()+ " : " + res);
        doCommit();
        return insertThumbnail(thumbnail, album);

    }
    private boolean insertThumbnail(CTThumbnail thumbnail, String album) throws SQLException {
        String sql = String.format(SqlStatements.INSERT_NEW_THUMBNAIL_TO_ALBUM, album);
        Object[] args = {thumbnail.getThumbnailName(),thumbnail.getThumbnailHeight(), thumbnail.getThumbnailWidth(),thumbnail.getThumbnailData() };
        boolean res =insertQuery(sql, args);
        logger.info("Result of inserting thumbnail " +thumbnail.getThumbnailName()+ " : " + res);
        doCommit();
        return res;
    }
    public CTImage getImageFromAlbum(String albumName, String imageName) throws SQLException {
        CTImage image = new CTImage();
        String query = String.format(SqlStatements.SELECT_IMAGE_FROM_ALBUM, albumName);
        ResultSet rs = selectQuery(query, new String[] {"", imageName});
        while(rs.next()) {
            image.setImageName(rs.getString("image_name"));
            image.setImageSize(rs.getInt("Image_size"));
            image.setImageData(rs.getBytes("image"));
            image.setTitle(rs.getString("title"));
            image.setImageHeight(rs.getInt("height"));
            image.setImageWidth(rs.getInt("width"));
            image.setUserName(rs.getString("username"));
            image.setDate(rs.getString("date"));
            image.setLongitude(rs.getFloat("longitude"));
            image.setLatitude(rs.getFloat("latitude"));
            image.setAlbumName(albumName);
        }
        rs.close();
        closeConnection();
        return image;
    }
    public CTAlbumPreview getAlbumPreview(String albumName) throws SQLException{
        CTAlbum album = getAlbumWithoutImages(albumName);
        //TODO Maybe to improve to query instead of getting the whole album
        CTAlbumPreview preview = new CTAlbumPreview();
        preview.setName(albumName);
        preview.setNumberOfImages(album.getThumbnails().size());
        if(album.getThumbnails().size() > 0) {
            preview.setPreviewImg(album.getThumbnails().get(0).getThumbnailData());
        } else {
            ImageUtils imgU = new ImageUtils();
            try {
                preview.setPreviewImg(imgU.getDefaultAlbumImage());
            } catch (IOException e) {
                logger.warn(e);
            }
        }
        return preview;
    }
    public CTAlbum getAlbumWithoutImages(String albumName) throws SQLException {
        CTAlbum album = new CTAlbum();
        ResultSet album_req_rs = selectQuery(String.format(SqlStatements.SELECT_ALBUM_FROM_ALBUMS, albumName));
        album_req_rs.next();
        album.setCreationDate(album_req_rs.getString("creation"));
        album.setExpirationDate(album_req_rs.getString("expiration"));
        album.setDescription(album_req_rs.getString("description"));
        album.setCreator(album_req_rs.getString("creator"));
        album.setName(albumName);
        album.getParticipants().addAll(Arrays.asList(album_req_rs.getString("participants").split(",")));
        album_req_rs.close();

        ResultSet thumbs_rs = selectQuery(String.format(SqlStatements.SELECT_ALL_THUMBNAILS_FROM_ALBUM, albumName));
        while(thumbs_rs.next()){
            CTThumbnail thumbnail = new CTThumbnail();
            thumbnail.setThumbnailName(thumbs_rs.getString("thumb_name"));
            thumbnail.setThumbnailWidth(thumbs_rs.getInt("width"));
            thumbnail.setThumbnailHeight(thumbs_rs.getInt("height"));
            thumbnail.setThumbnailData(thumbs_rs.getBytes("thumbnail"));
            album.getThumbnails().add(thumbnail);
        }
        thumbs_rs.close();


        return album;
    }





    // ############################### Interfaces ####################################
    public ResultSet selectQuery(String sql) throws SQLException {
        return doSqlStatementReturnResultSet(sql);
    }
    public ResultSet selectQuery(String sql, Object[] args) throws SQLException {
        return doSqlStatementReturnResultSet(sql, args);
    }
    public boolean updateQuery(String update) throws SQLException{
        boolean res = doSqlStatementReturnBoolean(update);
        return !res;
    }
    public boolean updateQuery(String update, Object[] args) throws SQLException{
        boolean res = doSqlStatementReturnBoolean(update, args);
        return !res;
    }
    public boolean insertQuery(String insert) throws SQLException{
        boolean res = doSqlStatementReturnBoolean(insert);
        return !res;
    }
    public boolean insertQuery(String insert, Object[] args) throws SQLException{
        boolean res = doSqlStatementReturnBoolean(insert, args);
        return !res;
    }
    public boolean deleteQuery(String delete) throws SQLException{
        boolean res = doSqlStatementReturnBoolean(delete);
        return !res;
    }
    public boolean deleteQuery(String delete, Object[] args) throws SQLException{
        boolean res = doSqlStatementReturnBoolean(delete, args);
        return !res;
    }
    // ############################### SQL ###########################################
    private ResultSet doSqlStatementReturnResultSet(String sql, Object[] args) throws SQLException {
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
            else if (args[i].getClass().equals(XMLGregorianCalendar.class)){
                ((PreparedStatement)this.statement).setDate(i, fromXMLDateToDate((XMLGregorianCalendar) args[i]));
            }
            else if (args[i].getClass().equals(Date.class)){
                ((PreparedStatement)this.statement).setDate(i,(Date) args[i]);
            }
            else if (args[i].getClass().equals(java.util.Date.class)){
                Date date = new Date(((java.util.Date)args[i]).getTime());
                ((PreparedStatement)this.statement).setDate(i, date);
            }
            else
                throw new SQLException("Not implemented");
        }
        logger.info("Executing Query " + sql);
        return ((PreparedStatement)this.statement).executeQuery();
    }
    private ResultSet doSqlStatementReturnResultSet(String sql) throws SQLException{
        this.statement = this.conn.createStatement();
        logger.info("Executing Query " + sql);
        return this.statement.executeQuery(sql);
    }
    private boolean doSqlStatementReturnBoolean(String sql, Object[] args) throws SQLException {
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
            else if (args[i].getClass().equals(XMLGregorianCalendar.class)){
                ((PreparedStatement)this.statement).setDate(i, fromXMLDateToDate((XMLGregorianCalendar) args[i]));
            }
            else if (args[i].getClass().equals(Date.class)){
                ((PreparedStatement)this.statement).setDate(i,(Date) args[i]);
            }
            else if (args[i].getClass().equals(java.util.Date.class)){
                Date date = new Date(((java.util.Date)args[i]).getTime());
                ((PreparedStatement)this.statement).setDate(i, date);
            }
            else
                throw new SQLException("Not implemented");
        }
        logger.info("Executing Query " + sql);
        return ((PreparedStatement)this.statement).execute();
    }
    private boolean doSqlStatementReturnBoolean(String sql) throws SQLException {
        this.statement = this.conn.createStatement();
        logger.info("Executing Query " + sql);
        return this.statement.execute(sql);
    }
    private void doCommit() throws SQLException {
        this.conn.commit();
    }
    // ############################### UTILS ##############################################
    private XMLGregorianCalendar fromDateTODateXml(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e) {
            return null;
        }
    }
    private Date fromXMLDateToDate(XMLGregorianCalendar date) {
        return new Date( date.toGregorianCalendar().getTimeInMillis());
    }
    private CTThumbnail createThumbnail(CTImage image ) throws IOException {
        ImageUtils imageUtils = new ImageUtils();
        CTThumbnail thumbnail = new CTThumbnail();
        thumbnail.setThumbnailData(imageUtils.createThumbnail(image.getImageData(), ImageUtils.THUMBNAIL_IMG_SIZE, ImageUtils.THUMBNAIL_IMG_SIZE));
        thumbnail.setThumbnailHeight(ImageUtils.THUMBNAIL_IMG_SIZE);
        thumbnail.setThumbnailWidth(ImageUtils.THUMBNAIL_IMG_SIZE);
        thumbnail.setThumbnailName(image.getImageName());
        return thumbnail;
    }

    public boolean updateUser(User user) throws SQLException, ClassNotFoundException {
        String sql = String.format(SqlStatements.UPDATE_USER_PROFILE, user.getUserName());
        Object[] values = {"",user.getUserName(), user.getPassword(), user.getBirthday(), user.getJoinDate(),
                user.getProfileImage(), user.getEmail(), String.join(",",user.getFriends()), user.getDescription()};
        createConnection();
        boolean success = updateQuery(sql, values);
        closeConnection();
        return success;
    }

    public User getUser(String username) throws SQLException, ClassNotFoundException {
        createConnection();
        String[] o = {"", username};
        ResultSet rs = selectQuery(SqlStatements.SELECT_USER_FROM_USERS, o);
        if(rs.next()) {
            User old = new User();
            old.setUserName(username);
            old.setBirthday(rs.getString("birthday"));
            old.setPassword(rs.getString("password"));
            old.setProfileImage(rs.getBytes("profile_img"));
            old.setEmail(rs.getString("email"));
            old.setJoinDate(rs.getString("joined"));
            old.setDescription(rs.getString("info"));
            String friends = rs.getString("friends");
            if(!friends.trim().isEmpty())
                old.getFriends().addAll(Arrays.asList(friends.split(",")));
            rs.close();
            closeConnection();
            return old;
        }
        rs.close();
        closeConnection();
        return null;
    }
}



//TODO
//https://stackoverflow.com/questions/15127100/store-and-retrieve-images-in-postgresql-using-java