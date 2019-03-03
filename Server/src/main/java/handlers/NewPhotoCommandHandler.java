//package handlers;
//
//import client.Client;
////import xsd.CommandRequest;
//import common.ConfigHolder;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
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
