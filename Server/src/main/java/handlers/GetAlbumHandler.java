package handlers;


//import xsd.CommandRequest;

import database.SqlStatements;
import xmls.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GetAlbumHandler extends  CommandHandler {


    @Override
    public ResponseMessage handle(RequestMessage request) {

        ResponseMessage responseMessage = new ResponseMessage();

        HeaderResponse header = new HeaderResponse();
        header.setCommand(request.getHeader().getCommand());
        header.setUserId(request.getHeader().getUserId());

        responseMessage.setHeader(header);
        GetAlbumRequestBody req_body = fromXmlToClass(request.getBody(), GetAlbumRequestBody.class);

        GetAlbumResponseBody responseBody = new GetAlbumResponseBody();
        List<CTImage> images = null;
        ResultSet resultSet = null;

        try {
            dbClient.createConnection();
            String sql = String.format(SqlStatements.SELECT_ALL_RECORDS, req_body.getAlbumName());
            resultSet = dbClient.doSqlStatement(sql);

            while (resultSet.next()) {
                CTImage img = new CTImage();
                img.setAlbumName(req_body.getAlbumName());
                //img.setUserName(resultSet.getString("user_name"));
                img.setImageName(resultSet.getString("image_name"));
                img.setUserID(resultSet.getString("user_id"));
                img.setImageLength(resultSet.getInt("length"));
                img.setImageWidth(resultSet.getInt("width"));
                img.setImageSize(resultSet.getInt("image_size"));
                img.setImageData(resultSet.getBytes("image"));
                responseBody.getImages().add(img);
            }
            dbClient.closeConnection();
        } catch (ClassNotFoundException | NullPointerException | SQLException e) {
            System.out.println(e.toString());
        } finally {
            if (resultSet != null) try {
                resultSet.close();
            } catch (SQLException ignore) {
            }
        }
        responseMessage.setBody(fromClassToXml(responseBody));
        return responseMessage;
    }
}
