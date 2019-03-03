
import database.DBClient;
import common.ResourcesHandler;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import xsd.Header;
import xsd.Message;
import xsd.Rules;
import xsd.albums.NewAlbumRequestBody;
import xsd.albums.NewAlbumResponseBody;

import javax.xml.bind.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class Main {
    public static Logger logger = LogManager.getLogger();
    public static void main(String args[]){
        logger.info("info");
        logger.debug("vdfd");
//        Main main3 = new Main();
//        try {
//            //main3.jaxbTest2();
//            main3.jaxbTest();
//            //init_tables();
//        } catch (Exception ex){
//            ex.printStackTrace();
//        }
    }

    public void jaxbTest()throws Exception {

        System.out.println(Message.class.getPackage().getName());

        JAXBContext jaxbCtxMsg = JAXBContext.newInstance(Message.class);
        Marshaller marshaller = jaxbCtxMsg.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        Header header = new Header();
        header.setCommand("CreateNewAlbum");
        header.setFromUserId("2189438943");


        Rules rules = new Rules();
        rules.setLatitude(282328982.383289832f);
        rules.setLongitude(282328982.383289832f);
        rules.setRadius(50);


        NewAlbumRequestBody requestBody = new NewAlbumRequestBody();
        requestBody.setAlbumName("Eilon");
        requestBody.setRules(rules);
        requestBody.setUserId("eilon47");

        NewAlbumResponseBody responseBody = new NewAlbumResponseBody();
        responseBody.setAlbumName("Eilon");
        responseBody.setUserId("eilon47");
        responseBody.setSucceeded(true);

        Message request = new Message();
        request.setHeader(header);
        request.setBody(requestBody);

        Message response = new Message();
        response.setHeader(header);
        response.setBody(responseBody);

        File requestF = new File( "request.xml" );
        File responseF = new File( "response.xml" );
        try {
            FileOutputStream fos = new FileOutputStream(requestF);
            marshaller.marshal(request, fos);
            fos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            FileOutputStream fos = new FileOutputStream(responseF);
            marshaller.marshal(response, fos);
            fos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void jaxbTest2() throws Exception{
        File file = new File("request.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(Message.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Message msg = (Message) unmarshaller.unmarshal(file);
        System.out.println(msg);

        File file2 = new File("response.xml");
        JAXBContext jaxbContext2 = JAXBContext.newInstance(Message.class);
        Unmarshaller unmarshaller2 = jaxbContext2.createUnmarshaller();
        Message msg2 = (Message) unmarshaller2.unmarshal(file);
        System.out.println(msg2);
    }
    private static void init_tables()throws  Exception {
        DBClient client = new DBClient();
        client.createConnection();
        JsonElement root = new JsonParser().parse(new FileReader(ResourcesHandler.getResourceFilePath(ResourcesHandler.init_tables)));
        JsonObject object = root.getAsJsonObject();
        Gson gson = new Gson();
        for(String table : object.keySet()){
            String[] keys = gson.fromJson(object.get(table), String[].class);
            //client.createTable(table, keys);
        }
        client.closeConnection();

    }



}
