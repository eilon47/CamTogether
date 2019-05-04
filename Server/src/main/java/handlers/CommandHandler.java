package handlers;

import database.DBClient;
import database.SqlStatements;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xmls.*;
import converters.IConverter;
import converters.XmlConverter;

import javax.xml.bind.JAXBException;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CommandHandler {
    protected static Logger logger = LogManager.getLogger("handlers");
    protected static DBClient dbClient = new DBClient();
    protected static XmlConverter converter = new XmlConverter();
    public abstract ResponseMessage handle(RequestMessage request);

    protected ResponseHeader createResponseHeader(RequestHeader headerRequest){
        ResponseHeader headerResponse = new ResponseHeader();
        headerResponse.setCommand(headerRequest.getCommand());
        headerResponse.setUsername(headerRequest.getUsername());
        headerResponse.setCommandSuccess(true);
        return headerResponse;
    }

    protected <T> T fromXmlToClass(String xml, Class<T> tClass){
        try {
            return converter.serializeFromString(xml, tClass);
        } catch (JAXBException ex){
            logger.warn("Failed creating class " + tClass.getName()+" from xml", ex);
            return null;
        }
    }

    protected <T> String fromClassToXml(T object){
        try {
            return converter.serializeToString(object);
        } catch (JAXBException ex){
            logger.warn("failed creating xml from class " + object.getClass().getName(), ex);
            return null;
        }
    }

    protected boolean isAuthorized(String album_name, String user) throws SQLException {
        logger.debug("connection with db created - executing select query to receive participants list");
        String sql = String.format(SqlStatements.SELECT_FROM_ALBUMS, "participants", "WHERE album_name='" + album_name +"'");
        ResultSet resultSet = dbClient.selectQuery(sql);
        String participants = null;
        boolean res = false;
        if (resultSet.next())
            participants = resultSet.getString("participants");
        if(participants!=null)
            res = participants.contains(user);
        resultSet.close();
        dbClient.closeConnection();
        return res;
    }
}
