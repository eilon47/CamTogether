package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import xmls.*;
import javafx.scene.control.TextField;
import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Created by green on 3/17/2019.
 */
public class CreateNewAlbum {
    @FXML
    private TextField name;
    @FXML
    private Button submit;

    private String nameStr;
    private String response;

    public CreateNewAlbum() {
        this.name = null;
    }
    @FXML
    public void getName(){
        if (name != null)
            nameStr = name.toString();
            System.out.print(name.toString());
    }
    public String getResponse() {
        return this.response;
    }
    @FXML
    private void submitRequest() throws JAXBException, IOException {
        try {
            RequestMessage rm = new RequestMessage();
            //creating header
            HeaderRequest hr = new HeaderRequest();
            hr.setUserId("dandan");
            hr.setCommand(CommandsEnum.CREATE_NEW_ALBUM);
            //creating body
            NewAlbumRequestBody narb = new NewAlbumRequestBody();
            narb.setRules(new Rules());
            narb.setManager("dandan");
            narb.setAlbumName(this.name.getText());
            //initializing the request
            rm.setHeader(hr);
            rm.setBody(XsdUtils.serializeToXML(narb));

            //parse request to String format
            String request = XsdUtils.serializeToXML(rm);
            System.out.print(request);
            //TODO client should be global
            // send to server
            //CTClient ctClient = new CTClient();
            //ctClient.sendMessage(request);
            //this.response = ctClient.getMessage();

        } catch (JAXBException  e) {
            e.printStackTrace();
            submit.getScene().getWindow().hide();
        }
        submit.getScene().getWindow().hide();
    }



}
