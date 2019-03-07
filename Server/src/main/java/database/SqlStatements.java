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
                    "album_id   char(50)  NOT NULL, " +
                    "album_name   char(50)  NOT NULL, " +
                    "user_id    char(50)   NOT NULL, " +
                    "participants text" +
                    ");",

            "CREATE TABLE rules (" +
                    "album_id  char(50)  NOT NULL, " +
                    "location   boolean NOT NULL, " +
                    "longitude float(24), " +
                    "latitude  float(24), " +
                    "radius  integer," +
                    "time   boolean NOT NULL, " +
                    "start_date   char(20), " +
                    "end_date   char(20), " +
                    "start_hour   char(20), " +
                    "end_hour   char(20)" +
                    ");",

            "CREATE TABLE users (" +
                    "user_id    char(50) NOT NULL," +
                    "user_name  char(50) NOT NULL," +
                    "albums_manager   char(50)," +
                    "album_part char(50)," +
                    "info   char(50)" +
                    ");"
    };
    public static final String NEW_ALBUM_CREATION = "CREATE TABLE %s (" +
            "image_name text, " +
            "image_size integer, " +
            "image bytea, " +
            "user_id char(50), " +
            "length integer, " +
            "width integer" +
            ");";

    //INSERT
    public static final String INSERT_NEW_ALBUM_TO_ALBUMS_TABLE = "INSERT INTO albums VALUES (?, ?, ?, ?);";
    public static final String INSERT_NEW_USER_TO_USERS_TABLE = "INSERT INTO users VALUES (?, ?, ?, ?, ?);";
    public static final String INSERT_NEW_RULES_TO_RULES_TABLE = "INSERT INTO rules VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    public static final String INSERT_NEW_IMAGE_TO_ALBUM = "INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?);";

    //SELECT
    public static final String SELECT_IMAGE_FROM_ALBUM = "SELECT * FROM %s WHERE image_name = ?;";
    public static final String SELECT_RULES_FOR_ALBUM = "SELECT * FROM rules WHERE album_id = ?;";
    public static final String SELECT_USER_FROM_USERS = "SELECT * FROM users WHERE user_id = ?;";




}
