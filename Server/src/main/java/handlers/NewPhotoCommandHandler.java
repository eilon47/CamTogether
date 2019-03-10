package handlers;

//import xsd.CommandRequest;
import database.CTImageRecord;
import database.DBClient;
import database.SqlStatements;
import xsd.AddNewImage.AddNewImageResponseBody;
import xsd.Header;
import xsd.Message;
import xsd.AddNewImage.AddNewImageRequestBody;
import xsd.Rules;
import xsd.albums.NewAlbumRequestBody;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewPhotoCommandHandler extends CommandHandler {

    DBClient db_client = new DBClient();

    @Override
    public Message handle(Message request) throws JAXBException {
        Message returnMessage = new Message();
        //create header of message
        Header header = new Header();
        header.setCommand(request.getHeader().getCommand());
        header.setFromUserId(request.getHeader().getFromUserId());
        returnMessage.setHeader(header);

        AddNewImageRequestBody req_body = (AddNewImageRequestBody) request.getBody();

        AddNewImageResponseBody resbody = new AddNewImageResponseBody();
        resbody.setUserId(request.getHeader().getFromUserId());
        resbody.setSucceeded(false);
        try {
            CTImageRecord CTimage = new CTImageRecord();
            CTimage.setUser_id(header.getFromUserId());
            CTimage.setAlbum(req_body.getAlbum_name());
            CTimage.setData(req_body.getData());
            CTimage.setName(((AddNewImageRequestBody) request.getBody()).getImage_name());
            if (!alreadyExist(CTimage.getAlbum(),CTimage.getName())) {

                db_client.createConnection();
                String sql = SqlStatements.INSERT_NEW_IMAGE_TO_ALBUM;
                Object[] values = new Object[7];
                values[0] = "";
                values[1] = "dandan";
                values[2] = "dandan";
                values[3] = CTimage.getData();
                values[4] = CTimage.getName();
                values[5] = CTimage.getDataSize();
                values[6] = CTimage.getUser_id();

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
        return false;
    }

    private static class B {

        public static void main(String args[]) {
            try {
                JAXBContext jaxbCtx = JAXBContext.newInstance(Message.class);
                Marshaller marshaller = jaxbCtx.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                NewPhotoCommandHandler handler = new NewPhotoCommandHandler();

                Header header = new Header();
                header.setFromUserId("danielG");
                header.setCommand("fkdl");

                AddNewImageRequestBody body = new AddNewImageRequestBody();
                body.setAlbum_name("testingAlbum");
                body.setImage_name("new_image");
                body.setData(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

                Message msg = new Message();
                msg.setHeader(header);
                msg.setBody(body);

                StringWriter sw = new StringWriter();
                marshaller.marshal(msg, sw);
                System.out.println(sw.toString());

                handler.handle(msg);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

