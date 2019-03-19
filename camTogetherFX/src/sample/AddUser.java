package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import xmls.*;
import javafx.scene.control.TextField;
import javax.xml.bind.JAXBException;
import java.io.IOException;


public class AddUser {
    @FXML
    private TextField name;
    @FXML
    private TextField albumName;
    @FXML
    private Button submit;

    private String response;
    public AddUser() {
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
            HeaderRequest hr = new HeaderRequest();

            hr.setUserId(this.userID);
            hr.setCommand(CommandsEnum.ADD_USER_TO_ALBUM);
            //creating body
            AddUserToAlbumRequestBody auta = new AddUserToAlbumRequestBody();
            User user = new User();
            user.setUserName(this.name.getText());
            user.setUserID(this.name.getText());
            auta.setAddToAlbum(this.albumName.getText());
            auta.setUserToAdd(user);
            //initializing the request
            rm.setHeader(hr);
            rm.setBody(XsdUtils.serializeToXML(auta));

            //parse request to String format
            String request = XsdUtils.serializeToXML(rm);
            System.out.print(request);
            //TODO client should be global
            //send to server
            CTClient ctClient = new CTClient();
            ctClient.createConnection();
            ctClient.sendMessage(request);
            ctClient.getInt();
            this.response = ctClient.getMessage();
            ctClient.closeConnection();


        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            submit.getScene().getWindow().hide();
        }
        submit.getScene().getWindow().hide();
    }
}
