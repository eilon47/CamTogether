package handlers;

import database.SqlStatements;
import xmls.*;

import java.sql.SQLException;

public class UpdateAlbumRulesHandler extends CommandHandler {
    @Override
    public ResponseMessage handle(RequestMessage request) {
        ResponseMessage response = new ResponseMessage();
        //create header of message
        response.setHeader(createHeaderResponse(request.getHeader()));
        UpdateRulesRequestBody requestBody = fromXmlToClass(request.getBody(), UpdateRulesRequestBody.class);

        String sql = String.format(SqlStatements.UPDATE_RULES_FOR_ALBUM, requestBody.getAlbum());
        Rules rules = requestBody.getNewRules();
        boolean location = nullityCheck(rules.getRadius()) && nullityCheck(rules.getLatitude()) && nullityCheck(rules.getLongitude());
        boolean time = nullityCheck(rules.getEndTime()) && nullityCheck(rules.getStartTime());
        Object[] values = {location, rules.getLongitude(), rules.getLatitude(), rules.getRadius(), time, rules.getStartTime(), rules.getEndTime()};
        try {
            dbClient.createConnection();
            boolean res = dbClient.dynamicQuery(sql, values);
            dbClient.closeConnection();
            response.getHeader().setCommandSuccess(res);
            UpdateRulesResponseBody responseBody = new UpdateRulesResponseBody();
            responseBody.setSuccess(res);
            response.setBody(fromClassToXml(responseBody));
            return response;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            logger.warn("error in updating rules", e);
        }
        response.getHeader().setCommandSuccess(false);
        return response;
    }
    private boolean nullityCheck(Object o){
        return o != null;
    }

}
