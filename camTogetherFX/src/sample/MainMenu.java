package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import xmls.*;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import javax.xml.soap.Text;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 */
public class MainMenu implements Initializable {
    @FXML
    private Button refreshList;
    @FXML
    private ScrollPane pics;
    @FXML
    private Button Connect;
    @FXML
    private Button addUser;
    @FXML
    private Button viewAlbums;
    @FXML
    private Button createNewAlbum;
    @FXML
    private Button addPhoto;
    @FXML
    private AnchorPane root;
    @FXML
    private ListView<String> albumList;
    @FXML
    private TextField insertUser;

    private String userID;

    private File photo;
    @FXML
    private String selectedAlbum;
    private String response;
    private String userName;
    private ObservableList<String> albums;
    public MainMenu() throws IOException {
    }

    @FXML
    public void addUserToAlbum() {
        return;
    }

    public String getSelectedAlbum() {
        return this.selectedAlbum;
    }
    public String getUserID() {
        return this.selectedAlbum;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.albums = FXCollections.<String>observableArrayList();
        this.albumList.setItems(albums);
        this.root = new AnchorPane();
        this.userID = XsdUtils.userID;
        this.selectedAlbum = XsdUtils.albumName;
    }
    @FXML
    private void Connect() {
        try {
            CTClient client = new CTClient();
            client.createConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void RefreshList() throws JAXBException, IOException {
        GetAlbumsListRequestBody b_r = new GetAlbumsListRequestBody();
        RequestHeader h_r = new RequestHeader();
        h_r.setUserId(this.userID);
        h_r.setCommand(CommandsEnum.GET_ALBUMS_LIST);

        RequestMessage rm = new RequestMessage();
        rm.setHeader(h_r);
        rm.setBody(XsdUtils.serializeToXML(b_r));

        String request = XsdUtils.serializeToXML(rm);
        CTClient ctClient = new CTClient();
        ctClient.createConnection();
        ctClient.sendMessage(request);
        ctClient.getInt();
        this.response = ctClient.getMessage();
        ResponseMessage resMessage = XsdUtils.serializeFromXml(this.response, ResponseMessage.class);
        if (resMessage.getHeader().isCommandSuccess()) {
            this.albums.clear();
            this.albums.addAll((XsdUtils.serializeFromXml(resMessage.getBody(),GetAlbumsListResponseBody.class)).getAlbums());
        }
        ctClient.closeConnection();
    }
    @FXML
    private void CreateNewAlbum() throws IOException, JAXBException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateNewAlbumC.fxml"));
        Stage stage = new Stage();
        stage.initOwner(createNewAlbum.getScene().getWindow());
        stage.setScene(new Scene(loader.load()));

        // showAndWait will block execution until the window closes...
        stage.showAndWait();
        String name;
        CreateNewAlbum controller = loader.getController();
        ResponseMessage rm = XsdUtils.serializeFromXml(controller.getResponse(),ResponseMessage.class);
        if (rm.getHeader().isCommandSuccess()) {
            name = controller.getName();
            if (name != null) {
                albums.add(name);
            }
        }
    }
    @FXML
    private void viewAlbum(ActionEvent ae) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AlbumViewC.fxml"));
        Stage stageTheLabelBelongs = (Stage) viewAlbums.getScene().getWindow();
        //stageTheLabelBelongs.setMaxWidth(600);
        //stageTheLabelBelongs.setMaxHeight(400);
        Scene scene = new Scene(root,600,400);
        stageTheLabelBelongs.setScene(scene);
        stageTheLabelBelongs.show();
    }
    @FXML
    public void AddPhotoToAlbum() throws IOException, JAXBException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        Stage stage = (Stage) Connect.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            sendFile(file);
        }
    }
    @FXML
    public void AddUserToAlbum() throws IOException, JAXBException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddUserC.fxml"));
        Stage stage = new Stage();
        stage.initOwner(addUser.getScene().getWindow());
        stage.setScene(new Scene(loader.load()));

        // showAndWait will block execution until the window closes...
        stage.showAndWait();
        String name;
        AddUser controller = loader.getController();
        ResponseMessage rm = XsdUtils.serializeFromXml(controller.getResponse(),ResponseMessage.class);
        //if (rm.getHeader().isCommandSuccess()) {}
    }

    private void sendFile(File file) {
        this.photo = file;
        try {
            CTImage img = create_CTimage(photo);
            NewImageRequestBody b_r = new NewImageRequestBody();
            RequestHeader h_r = new RequestHeader();
            h_r.setUserId(img.getUserID());
            h_r.setCommand(CommandsEnum.ADD_NEW_PHOTO);

            b_r.setAlbum(img.getAlbumName());
            b_r.setImage(img);
            RequestMessage rm = new RequestMessage();
            rm.setHeader(h_r);
                rm.setBody(XsdUtils.serializeToXML(b_r));

            String request = XsdUtils.serializeToXML(rm);
            CTClient ctClient = new CTClient();
            ctClient.createConnection();
            ctClient.sendMessage(request);
            int a = ctClient.getInt();
            this.response = ctClient.getMessage();
            ctClient.closeConnection();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }

    }

    public CTImage create_CTimage(File f) {
        try {
            CTImage img = new CTImage();
            BufferedImage imgg = ImageIO.read(f);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( imgg, "jpg", baos );
            baos.flush();
            byte[] imageInByte = baos.toByteArray();

            img.setImageSize(imgg.getWidth()*imgg.getHeight());
            img.setImageName(f.getName());
            img.setImageData(imageInByte);
            img.setAlbumName(this.selectedAlbum);
            img.setUserName(this.userID);
            img.setImageLength(imgg.getHeight());
            img.setImageWidth(imgg.getWidth());
            return img;

        }catch (IOException e) {
            return null;
        }
    }
}
