package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import xmls.*;
import javafx.scene.control.TextField;
import javax.xml.bind.JAXBException;
import java.io.IOException;


public class CreateNewAlbum {
    @FXML
    private TextField name;
    @FXML
    private Button submit;

    private String response;
    public CreateNewAlbum() {
        this.name = null;
    }
    public String getResponse() {
        return this.response;
    }

    @FXML
    public String getName(){
        if (name != null)
            return name.getText();
        return null;
    }
    private String userID;
    @FXML
    private void submitRequest() throws JAXBException, IOException {
        //FXMLLoader root = FXMLLoader.load(getClass().getResource("MainMenuC.fxml"));
        //this.userID = ((MainMenu)root.getController()).getUserID();
        this.userID = XsdUtils.userID;
        try {
            RequestMessage rm = new RequestMessage();
            //creating header
            RequestHeader hr = new RequestHeader();
            hr.setUserId(this.userID);
            hr.setCommand(CommandsEnum.CREATE_NEW_ALBUM);
            //creating body
            NewAlbumRequestBody narb = new NewAlbumRequestBody();
            narb.setRules(new Rules());
            narb.setManager(this.userID);
            narb.setAlbumName(this.name.getText());
            //initializing the request
            rm.setHeader(hr);
            rm.setBody(XsdUtils.serializeToXML(narb));

            //parse request to String format
            String request = XsdUtils.serializeToXML(rm);
            System.out.print(request);
            //TODO client should be global
            //send to server
            CTClient ctClient = new CTClient();
            ctClient.createConnection();
            ctClient.sendMessage(request);
            int a = ctClient.getInt();
            this.response = ctClient.getMessage();
            ctClient.closeConnection();

        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            submit.getScene().getWindow().hide();
        }
        submit.getScene().getWindow().hide();
    }
}
