package CamTogether.RestApi.services;


import CamTogether.RestApi.client.CamTogetherClient;
import client.Client;
import converters.JsonConverter;
import converters.XmlConverter;
import xmls.RequestMessage;
import xmls.ResponseMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.xml.bind.JAXBException;
import java.io.IOException;

public abstract class AbstractService {
    protected static Logger logger = LogManager.getLogger("services");
    protected XmlConverter xmlConverter = new XmlConverter();
    protected JsonConverter jsonConverter = new JsonConverter();

    protected ResponseMessage messageToServerAndResponse(RequestMessage requestMessage, Object body) {
        CamTogetherClient client = new CamTogetherClient(18080, "0.0.0.0");
        try {
            client.createConnection();
            requestMessage.setBody(xmlConverter.serializeToString(body));
            String xmlMessage = xmlConverter.serializeToString(requestMessage);
            client.sendMessage(xmlMessage);
            String response = client.getMessage();
            client.closeConnection();
            return xmlConverter.serializeFromString(response, ResponseMessage.class);
        } catch (IOException e) {
            logger.warn("Failed to create/close connection with server", e);
            e.printStackTrace();
            return new ResponseMessage();
        } catch (JAXBException e) {
            logger.warn("Failed to create message from object", e);
            e.printStackTrace();
            return new ResponseMessage();
        }
    }

}
