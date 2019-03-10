package handlers;

//import xsd.CommandRequest;
import database.CTImageRecord;
import database.DBClient;
import database.SqlStatements;
import xsd.AddNewImage.AddNewImageResponseBody;
import xsd.Header;
import xsd.Message;
import xsd.AddNewImage.AddNewImageRequestBody;

import javax.xml.bind.JAXBException;
import java.sql.ResultSet;
import java.sql.SQLException;

//import java.io.IOException;
//
//public class NewPhotoCommandHandler {
//    private static Client imageAnalysisClient = new Client(ConfigHolder.getCommunicationIntProp("iaserverport", 19090),
//                                                            ConfigHolder.getCommunicationProp("iaserverip", "127.0.0.1"));
//
//    private static Logger logger = LogManager.getLogger();
//    public CommandResponse handle(CommandRequest request) {
//        logger.info("Handling request " + request.toString());
//        String img = extractImageFromRequest(request.getRequest());
//        int grade = 100;
//        try {
//            logger.info("Sending image to analyzer");
//            imageAnalysisClient.sendMessage(img);
//            grade = Integer.getInteger(imageAnalysisClient.getMessage());
//            logger.info("Grade received from analyzer is " + grade);
//        } catch (IOException ioe){
//            logger.warn("Could not receive information from analyzer", ioe);
//        }
//        if(grade > 50){
//            logger.info("Updating picture in the DB");
//            return new CommandResponse(request.getCmd(), "", true, request.getFrom());
//        }else {
//            logger.info("Image did not pass the threshold");
//            return new CommandResponse(request.getCmd(), "Fail", false, request.getFrom());
//        }
//    }
//
//    public String extractImageFromRequest(String request){
//        String img = request;
//        return img;
//    }
//}

public class NewPhotoCommandHandler extends CommandHandler {

    DBClient db_client = new DBClient();

    @Override
    public Message handle(Message request) throws JAXBException {
        Message returnMessage = new Message();
        Header header = new Header();
        header.setCommand(request.getHeader().getCommand());
        header.setFromUserId(request.getHeader().getFromUserId());
        returnMessage.setHeader(header);
        AddNewImageRequestBody b = (AddNewImageRequestBody) request.getBody();
        AddNewImageResponseBody resbody = new AddNewImageResponseBody();
        resbody.setUserId(request.getHeader().getFromUserId());
        resbody.setSucceeded(false);
        try {
            CTImageRecord CTimage = new CTImageRecord();
            CTimage.setUser_id(header.getFromUserId());
            CTimage.setAlbum(b.getAlbum_name());
            CTimage.setData(b.getData());

            if (!alreadyExist(CTimage.getAlbum(),CTimage.getName())) {

                db_client.createConnection();
                String sql = SqlStatements.INSERT_NEW_IMAGE_TO_ALBUM;
                Object[] values = new Object[6];

                values[0] = "";
                values[1] = "";
                values[2] = CTimage.getData();
                values[3] = CTimage.getName();
                values[4] = CTimage.getDataSize();
                values[5] = CTimage.getUser_id();

                ResultSet res = db_client.dynamicPrepareStatement(sql,values);
                if (!res.wasNull())
                    resbody.setSucceeded(true);
                returnMessage.setBody(resbody);
                return returnMessage;
            }
        } catch (SQLException | ClassNotFoundException e){
            resbody.setSucceeded(true);
            returnMessage.setBody(resbody);
            return returnMessage;
        }


        return null;
    }

    public boolean alreadyExist(String album_name, String image_name){
        return true;
    }
}

