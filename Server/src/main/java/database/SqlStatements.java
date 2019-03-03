package database;

public final class SqlStatements {
    public static final String NEW_ALBUM_ST = "CREATE TABLE %s (image_name text, image_size integer, image bytea, user_id char(50));";
    public static final String INSERT_NEW_IMAGE = "INSERT INTO %s VALUES (?, ?, ?, ?);";
    public static final String SELECT_IMAGE_FROM_TABLE = "SELECT * FROM %s WHERE image_name = ?;";
}
