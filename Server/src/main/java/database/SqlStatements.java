package database;

public final class SqlStatements {
    //GENERAL
    public static final String REMOVE_TABLE= "DROP %s;";
    public static final String UPDATE_TABLE_RECORD = "UPDATE %s SET %s WHERE %s;";
    public static final String DELETE_TABLE_RECORD = "DELETE FROM %s WHERE %s;";
    public static final String SELECT_ALL_RECORDS = "SELECT * FROM %s;";

    //CREATE
    public static final String[] INIT_BASIC_TABLES = {
            "CREATE TABLE albums (" +
                    "album_name   text  NOT NULL PRIMARY KEY, " +
                    "owner    text   NOT NULL, " +
                    "participants text, " +
                    "creation date, " +
                    "expiration date" +
                    ");",

            "CREATE TABLE rules (" +
                    "album_name  char(64)  NOT NULL   PRIMARY KEY, " +
                    "location   boolean NOT NULL, " +
                    "longitude float(24), " +
                    "latitude  float(24), " +
                    "radius  integer," +
                    "time   boolean NOT NULL, " +
                    "start_date   char(20), " +
                    "end_date   char(20) " +
                    ");",

            "CREATE TABLE users (" +
                    "username    char(64) NOT NULL PRIMARY KEY," +
                    "birthday  date," +
                    "joined date," +
                    "profile_img  bytea," +
                    "email text," +
                    "friends text,"+
                    "info   text" +
                    ");"
    };
    public static final String NEW_ALBUM_CREATION = "CREATE TABLE %s (" +
            "image_name text, " +
            "image_size integer, " +
            "image bytea, " +
            "title  text, " +
            "length integer, " +
            "width integer," +
            "username  text," +
            "date  date," +
            "longitude  numeric," +
            "latitude  numeric" +
            ");";

    public static final String NEW_ALBUM_THUMBNAIL_CREATION = "CREATE TABLE %s (" +
            "thumb_name text, " +
            "length integer, " +
            "width integer, "+
            "thumbnail bytea" +
            ");";

    //INSERT
    public static final String INSERT_NEW_THUMBNAIL_TO_ALBUM = "INSERT INTO %s_thumbs VALUES (?, ?, ?, ?);";
    public static final String INSERT_NEW_ALBUM_TO_ALBUMS_TABLE = "INSERT INTO albums VALUES (?, ?, ?, ?, ?);";
    public static final String INSERT_NEW_USER_TO_USERS_TABLE = "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?);";
    public static final String INSERT_NEW_RULES_TO_RULES_TABLE = "INSERT INTO rules VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    public static final String INSERT_NEW_IMAGE_TO_ALBUM = "INSERT INTO %s_imgs VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";


    //SELECT
    public static final String SELECT_ALL_THUMBNAILS_FROM_ALBUM = "SELECT * FROM %s_thumbs;";
    public static final String SELECT_IMAGE_FROM_ALBUM = "SELECT * FROM %s_imgs WHERE image_name = ?;";
    public static final String SELECT_RULES_FOR_ALBUM = "SELECT * FROM rules WHERE album_name = ?;";
    public static final String SELECT_USER_FROM_USERS = "SELECT * FROM users WHERE username = ?;";

    //UPDATE
    public static final String UPDATE_TABLE = "UPDATE %s SET %s = '%s' WHERE %s;";


    public static final String[] newAlbumCreationSQLs(String album){
        String album_thumbnail = album + "_thumbs";
        String album_reg = album + "_img";
        return new String[]{String.format(NEW_ALBUM_CREATION, album_reg),
                String.format(NEW_ALBUM_THUMBNAIL_CREATION, album_thumbnail)};
    }

}
