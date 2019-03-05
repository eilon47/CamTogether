import database.DBClient;
import database.SqlStatements;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.SQLException;

public class Main {
    public static Logger logger = LogManager.getLogger();
    public static void main(String args[]) {
        try {
            init_tables();
        } catch (SQLException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }

    }
    private static void init_tables()throws SQLException, ClassNotFoundException {
        DBClient client = new DBClient();
        client.createConnection();
        for(String table : SqlStatements.INIT_TABLES){
            client.createTableFromString(table);
        }
        client.closeConnection();

    }



}
