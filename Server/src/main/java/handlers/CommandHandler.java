package handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xmls.*;
import xsd.XsdUtils;

import javax.xml.bind.JAXBException;

public abstract class CommandHandler {
    protected static Logger logger = LogManager.getLogger("handlers");
    public abstract ResponseMessage handle(RequestMessage request);

    protected HeaderResponse createHeaderResponse(HeaderRequest headerRequest){
        HeaderResponse headerResponse = new HeaderResponse();
        headerResponse.setCommand(headerRequest.getCommand());
        headerResponse.setUserId(headerRequest.getUserId());
        headerResponse.setCommandSuccess(true);
        return headerResponse;
    }

    protected <T> T fromXmlToClass(String xml, Class<T> tClass){
        try {
            return XsdUtils.serializeFromXml(xml, tClass);
        } catch (JAXBException ex){
            logger.warn("Failed creating class from xml", ex);
            return null;
        }
    }

    protected <T> String fromClassToXml(T object){
        try {
            return XsdUtils.serializeToXML(object);
        } catch (JAXBException ex){
            logger.warn("failed creating xml from class", ex);
            return null;
        }
    }

//    public static void main(String[] args){
//        try {
//            RequestMessage requestMessage = new RequestMessage();
//            HeaderRequest headerRequest = new HeaderRequest();
//           // headerRequest.setCommand("Command");
//            headerRequest.setUserId("eilon");
//            NewAlbumRequestBody requestBody = new NewAlbumRequestBody();
//            requestBody.setAlbumName("album");
//            requestBody.setManagers("eilon");
////            Rules rules = new Rules();
////            rules.setAltitude(29302.43F);
////            rules.setLongitude(32903.6F);
////            rules.setRadius(1);
////            rules.setEndTime();
////            rules.setStartTime();
//
//            String body = XsdUtils.serializeToXML(requestBody);
//            System.out.println(body);
//            requestMessage.setBody(body);
//            requestMessage.setHeader(headerRequest);
//            String xml = XsdUtils.serializeToXML(requestMessage);
//            System.out.println(xml);
//
//
//            RequestMessage requestMessage1 = XsdUtils.serializeFromXml("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
//                    "<RequestMessage xmlns=\"xsd\">\n" +
//                    "    <Header>\n" +
//                    "        <UserId>eilon</UserId>\n" +
//                    "        <Command>Command</Command>\n" +
//                    "    </Header>\n" +
//                    "    <Body>&lt;?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?&gt;\n" +
//                    "&lt;NewAlbumRequestBody xmlns=\"xsd\"&gt;\n" +
//                    "    &lt;AlbumName&gt;album&lt;/AlbumName&gt;\n" +
//                    "    &lt;Managers&gt;eilon&lt;/Managers&gt;\n" +
//                    "&lt;/NewAlbumRequestBody&gt;\n" +
//                    "</Body>\n" +
//                    "</RequestMessage>", RequestMessage.class);
//
//            NewAlbumRequestBody body1 = XsdUtils.serializeFromXml(requestMessage1.getBody(), NewAlbumRequestBody.class);
//
//            System.out.println(body1.getAlbumName());
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
