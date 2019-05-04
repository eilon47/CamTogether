package database;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.largeobject.LargeObjectManager;
import xmls.*;

import javax.imageio.ImageIO;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
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


    //##################### SELECT ###################################

    public ResultSet selectQuery(String selection, String table, String cond)throws SQLException{
        String sql = "SELECT " + selection + " FROM " + table;
        if (cond != null){
            sql += (" WHERE " + cond + ";");
        } else {
            sql += ";";
        }

        this.statement = this.conn.createStatement();
        logger.info("Executing Query " + sql);
        ResultSet rs = this.statement.executeQuery(sql);
        return rs;
    }

    public ResultSet selectQuery(String selection, String table)throws SQLException {
        return selectQuery(selection, table, null);
    }

    public Rules getAlbumRules(String album) throws SQLException, ClassNotFoundException {
        this.createConnection();
        Rules rules = null;
        ResultSet rs = this.doSqlStatement(String.format(SqlStatements.SELECT_RULES_FOR_ALBUM, album));
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
        closeConnection();
        return rules;
    }

    public CTImage selectQueryForImg(String table, String imageName) throws SQLException, IOException{
        // All LargeObject API calls must be within a transaction block
        conn.setAutoCommit(false);
        // Get the Large Object Manager to perform operations with
        LargeObjectManager lobj = ((org.postgresql.PGConnection)conn).getLargeObjectAPI();
        String sql = String.format(SqlStatements.SELECT_IMAGE_FROM_ALBUM, table);
        logger.info("Executing Query " + sql);
        this.statement = conn.prepareStatement(sql);
        ((PreparedStatement)this.statement).setString(1, imageName);
        logger.debug(((PreparedStatement)this.statement));
        ResultSet rs = ((PreparedStatement)this.statement).executeQuery();
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
        // Now insert the row
        this.statement = conn.prepareStatement(String.format(SqlStatements.INSERT_NEW_IMAGE_TO_ALBUM, album));
        ((PreparedStatement)this.statement).setString(1, imageRecord.getImageName());
        ((PreparedStatement)this.statement).setInt(2, imageRecord.getImageSize());
        ByteArrayInputStream is = new ByteArrayInputStream(imageRecord.getImageData());
        ((PreparedStatement)this.statement).setBinaryStream(3, is, imageRecord.getImageSize());
        ((PreparedStatement)this.statement).setString(4, imageRecord.getTitle());
        ((PreparedStatement)this.statement).setInt(5, imageRecord.getImageHeight());
        ((PreparedStatement)this.statement).setInt(6, imageRecord.getImageWidth());
        ((PreparedStatement)this.statement).setString(7, imageRecord.getUserName());
        ((PreparedStatement)this.statement).setString(8, imageRecord.getDate());
        ((PreparedStatement)this.statement).setFloat(9, imageRecord.getLongitude());
        ((PreparedStatement)this.statement).setFloat(10, imageRecord.getLatitude());
        ((PreparedStatement)this.statement).setString(11, imageRecord.getAlbumName());
        System.out.println(((PreparedStatement)this.statement));
        ((PreparedStatement)this.statement).executeUpdate();
        this.conn.commit();
        boolean thumbnail_insert =  insertThumbnail(thumbnail, album);
        return thumbnail_insert;

    }

    private boolean insertThumbnail(CTThumbnail thumbnail, String album) throws SQLException {
        this.statement = conn.prepareStatement(String.format(SqlStatements.INSERT_NEW_THUMBNAIL_TO_ALBUM, album));
        ((PreparedStatement)this.statement).setString(1, thumbnail.getThumbnailName());
        ((PreparedStatement)this.statement).setInt(2, thumbnail.getThumbnailHeight());
        ((PreparedStatement)this.statement).setInt(3, thumbnail.getThumbnailWidth());
        ByteArrayInputStream is = new ByteArrayInputStream(thumbnail.getThumbnailData());
        ((PreparedStatement)this.statement).setBinaryStream(4, is);
        System.out.println(((PreparedStatement)this.statement));
        ((PreparedStatement)this.statement).executeUpdate();
        this.conn.commit();
        return true;
    }
    private CTThumbnail createThumbnail(CTImage image ) throws IOException {
        CTThumbnail thumbnail = new CTThumbnail();
        InputStream in = new ByteArrayInputStream(image.getImageData());
        BufferedImage buf_img = ImageIO.read(in);
        Image thumb = buf_img.getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);
        BufferedImage buf_thumb = new BufferedImage(thumb.getWidth(null), thumb.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bgr = buf_thumb.createGraphics();
        bgr.drawImage(thumb, 0, 0,null);
        bgr.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(buf_thumb, "jpg", baos );
        baos.flush();
        thumbnail.setThumbnailData(baos.toByteArray());
        baos.close();
        thumbnail.setThumbnailHeight(100);
        thumbnail.setThumbnailWidth(100);
        thumbnail.setThumbnailName(image.getImageName());
        return thumbnail;
    }

    public CTImage getImageFromAlbum(String albumName, String imageName) throws SQLException {
        CTImage image = new CTImage();
        String query = String.format(SqlStatements.SELECT_IMAGE_FROM_ALBUM, albumName);
        ResultSet rs = prepareStatementAllStrings(query, new String[] {imageName});
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
        CTAlbum album = getAlbum(albumName);
        CTAlbumPreview preview = new CTAlbumPreview();
        preview.setName(albumName);
        preview.setNumberOfImages(album.getImages().size());
        preview.setPreviewImg(album.getImages().size() == 0? null : album.getThumbnails().get(0));
        return preview;
    }
    public CTAlbum getAlbum(String albumName) throws SQLException {
        CTAlbum album = new CTAlbum();
        ResultSet album_req_rs = doSqlStatement(String.format(SqlStatements.SELECT_ALBUM_FROM_ALBUMS, albumName));
        album_req_rs.next();
        album.setCreationDate(album_req_rs.getString("creation"));
        album.setExpirationDate(album_req_rs.getString("expiration"));
        album.setDescription(album_req_rs.getString("description"));
        album.setCreator(album_req_rs.getString("creator"));
        album.setName(albumName);
        album.getParticipants().addAll(Arrays.asList(album_req_rs.getString("participants").split(",")));
        album_req_rs.close();

        ResultSet thumbs_rs = doSqlStatement(String.format(SqlStatements.SELECT_ALL_THUMBNAILS_FROM_ALBUM, albumName));
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

    // ####################### GENERAL ################################

    public ResultSet doSqlStatement(String sql) throws SQLException{
        this.statement = this.conn.createStatement();
        logger.info("Executing Query " + sql);
        return this.statement.executeQuery(sql);
    }

    public ResultSet prepareStatementAllStrings(String sql, String[] values) throws SQLException{
        if (values.length == 0)
            return doSqlStatement(sql);
        statement = this.conn.prepareStatement(sql);
        for(int i=1; i < values.length; i++){
            ((PreparedStatement)this.statement).setString(i, values[i]);
        }
        logger.info("Executing Query " + sql);
        return ((PreparedStatement)this.statement).executeQuery();
    }

    public boolean dynamicQuery(String sql, Object[] args) throws SQLException{
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
        ((PreparedStatement)this.statement).executeUpdate();

        return true;
    }

    public boolean updateQuery(String update) throws SQLException{
        this.statement = this.conn.createStatement();
        //Execute method returns false if its doing update query
        logger.info("Executing Query " + update);
        boolean res = this.statement.execute(update);
        return !res;
    }

    public XMLGregorianCalendar fromDateTODateXml(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e) {
            return null;
        }
    }

    public Date fromXMLDateToDate(XMLGregorianCalendar date) {
        return new Date( date.toGregorianCalendar().getTimeInMillis());
    }
}



//TODO
//https://stackoverflow.com/questions/15127100/store-and-retrieve-images-in-postgresql-using-java