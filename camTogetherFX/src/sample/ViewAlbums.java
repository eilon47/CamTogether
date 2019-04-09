package sample;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class ViewAlbums implements Initializable{
    private List<CTImage> albumImages;
    private String response;
    @FXML
    private Button back;
    @FXML
    private void goBack() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenuC.fxml"));
        Stage stageTheLabelBelongs = (Stage) back.getScene().getWindow();
        //stageTheLabelBelongs.setMaxWidth(600);
        //stageTheLabelBelongs.setMaxHeight(400);
        Scene scene = new Scene(root,600,400);
        stageTheLabelBelongs.setScene(scene);
        stageTheLabelBelongs.show();
    }
    @FXML
    private ScrollPane pane;
    @FXML
    private String selectedAlbum;
    @FXML
    private String userId;
    @FXML
    public void setAlbumName(String name) {
        this.selectedAlbum = name;
    }

    public void initialize(URL location, ResourceBundle resources) {


        try {
            //FXMLLoader root = FXMLLoader.load(getClass().getResource("MainMenuC.fxml"));
            drawAlbum();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void drawAlbum() throws IOException {
        HBox hb = new HBox();
        //this.selectedAlbum = marked;
        getAlbum(XsdUtils.albumName,XsdUtils.userID);
        List<CTImage> albumImages = getImages();
        ImageView[] pics = new ImageView[albumImages.size()];
        List<Image> images = new ArrayList<>();
        for (CTImage img : albumImages) {
            ByteArrayInputStream bais = new ByteArrayInputStream(img.getImageData());
            BufferedImage image = ImageIO.read(bais);
            images.add(SwingFXUtils.toFXImage(image, null));
        }
        for (int i = 0; i < albumImages.size(); i++) {
            pics[i] = new ImageView(images.get(i));
            pics[i].setFitWidth(100);
            pics[i].setPreserveRatio(true);
            hb.getChildren().add(pics[i]);
            this.pane.setContent(hb);
        }
    }
    @FXML
    public void getAlbum(String albumName, String userId) {
        try {
            RequestMessage rm = new RequestMessage();
            //creating header
            HeaderRequest hr = new HeaderRequest();
            hr.setUserId(userId);
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
            CTClient ctClient = new CTClient();
            ctClient.createConnection();
            ctClient.sendMessage(request);
            int len = ctClient.getInt();
            int i = 0;
            StringBuilder sb = new StringBuilder();
            while (i < len) {
                sb.append(ctClient.getMessage());
                i++;
            }
            ctClient.closeConnection();
            this.response = sb.toString();
            ResponseMessage resMessage = XsdUtils.serializeFromXml(this.response, ResponseMessage.class);
            GetAlbumResponseBody gres = XsdUtils.serializeFromXml(resMessage.getBody(),GetAlbumResponseBody.class);
            this.albumImages = gres.getImages();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }
    public List<CTImage> getImages(){
        return this.albumImages;
    }
}
