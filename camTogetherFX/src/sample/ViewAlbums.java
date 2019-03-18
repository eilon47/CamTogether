package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import xmls.*;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by green on 3/17/2019.
 */

public class ViewAlbums {
    private List<CTImage> albumImages;
    private String response;
    @FXML
    private Button back;
    @FXML
    private void goBack() throws IOException {
        back.getScene().getWindow().hide();
    }
    @FXML
    private ScrollPane pane;
    private String selectedAlbum;
    @FXML
    public void getAlbum(String albumName) {
        try {
            RequestMessage rm = new RequestMessage();
            //creating header
            HeaderRequest hr = new HeaderRequest();
            hr.setUserId("dandan");
            hr.setCommand(CommandsEnum.GET_ALBUM);
            //creating body
            GetAlbumRequestBody garb = new GetAlbumRequestBody();
            garb.setAlbumName(albumName);

            //initializing the request
            rm.setHeader(hr);
            rm.setBody(XsdUtils.serializeToXML(garb));

            //parse request to String format
            String request = XsdUtils.serializeToXML(rm);
            System.out.print(request);
            //TODO client should be global
            // send to server
            //CTClient ctClient = new CTClient();
            //ctClient.sendMessage(request);
            //this.response = ctClient.getMessage();

            //ResponseMessage resMessage = XsdUtils.serializeFromXml(this.response, ResponseMessage.class);
            //GetAlbumResponseBody gres = XsdUtils.serializeFromXml(resMessage.getBody(),GetAlbumResponseBody.class);
            //this.albumImages = gres.getImages();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    public List<CTImage> getImages(){
        return this.albumImages;
    }
}
