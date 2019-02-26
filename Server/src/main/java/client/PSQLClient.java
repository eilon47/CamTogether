package client;



import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PSQLClient {
    private Connection conn;
    private Statement statement;
    private static final String username = "picturex";
    private static final String password = "1234";
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/basedb";


    private void createConnection() throws ClassNotFoundException, SQLException{
        Class.forName(JDBC_DRIVER);
        this.conn = DriverManager.getConnection(DB_URL, username, password);
    }

    private void closeConnection() throws SQLException{
        this.conn.close();
    }

    public void createTable(String tableName,String primaryKey, String... keys) throws SQLException, ClassNotFoundException{
        this.statement = createConnectionAndGetStatement();
        StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (" + primaryKey);
        for(String key :keys){
            sql.append(", " + key);
        }
        sql.append(");");
        statement.execute(sql.toString());
        closeStatementAndConn();
    }

    public Statement createConnectionAndGetStatement() throws SQLException, ClassNotFoundException{
        this.createConnection();
        return this.conn.createStatement();
    }

    public void closeStatementAndConn() throws SQLException{
        this.statement.close();
        this.conn.close();
    }

    public ResultSet selectQuery(String selection, String table, String cond)throws SQLException, ClassNotFoundException{
        String sql = "SELECT " + selection + " FROM " + table + " WHERE " + cond;
        this.statement = createConnectionAndGetStatement();
        ResultSet rs = statement.executeQuery(sql);
        closeStatementAndConn();
        return rs;
    }

    public boolean updateOp(String table, String key, String value, String cond)throws SQLException, ClassNotFoundException{
        String sql = "UPDATE " + table + " SET " + key + "=" +value + " WHERE " + cond;
        return doOperation(sql);
    }

    public boolean insertToAllKeysOp(String table, String ... values) throws SQLException, ClassNotFoundException {
        return insertToAllKeysOp(table, 1, values);
    }

    public boolean insertToAllKeysOp(String table, int numOfRecords ,String ... values) throws SQLException, ClassNotFoundException{
        String sql = "INSERT INTO "+ table +" VALUES ";
        int vals_in_rec = values.length/numOfRecords;
        String vals[] = new String[numOfRecords];
        for(int i = 0; i < numOfRecords; i++){
            vals[i] = "(" + String.join(",", Arrays.copyOfRange(values, i*vals_in_rec, i*vals_in_rec + vals_in_rec)) + ")";
        }
        sql += String.join(", ", vals) + ";";
        System.out.println(sql);
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
        System.out.println(sql);
        return doOperation(sql);
    }

    public boolean deleteOp(String table, String cond) throws SQLException, ClassNotFoundException{
        String sql = "DELETE FROM " + table + " WHERE " + cond;
        return doOperation(sql);
    }

    private boolean doOperation(String sql) throws SQLException, ClassNotFoundException{
        this.statement = this.createConnectionAndGetStatement();
        this.conn.setAutoCommit(false);
        this.statement.execute(sql);
        this.conn.commit();
        closeStatementAndConn();
        return true;
    }

    public static void main(String args[]){
        PSQLClient psqlClient = new PSQLClient();
        try {
            String keys[] = new String[] {"id", "album"};
            psqlClient.insertToSpecificKeysOp("picturex_table",keys , "60", "'third'", "70", "'forth'", "80", "'fifth'");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
