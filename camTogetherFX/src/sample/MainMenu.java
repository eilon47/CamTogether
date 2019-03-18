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
import xmls.CTImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 */
public class MainMenu implements Initializable {

    private CTClient client = new CTClient();
    @FXML
    private ScrollPane pics;
    @FXML
    private Button Connect;
    @FXML
    private Button Disconnect;
    @FXML
    private Button viewAlbums;
    @FXML
    private Button createNewAlbum;
    @FXML
    private AnchorPane root;
    @FXML
    private ListView<String> albumList;

    private String selectedAlbum;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> albums = FXCollections.<String>observableArrayList();
        albums.addAll("ct0", "ct1", "ct2", "ct3", "ct4", "ct5", "ct6", "ct7", "ct8", "ct9", "ct10");
        this.albumList.setItems(albums);
        this.root = new AnchorPane();
    }
    @FXML
    private void Connect() {
        Stage stage = (Stage) Connect.getScene().getWindow();
        try {
            client.createConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void CreateNewAlbum() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateNewAlbumC.fxml"));
        Stage stage = new Stage();
        stage.initOwner(createNewAlbum.getScene().getWindow());
        stage.setScene(new Scene(loader.load()));

        // showAndWait will block execution until the window closes...
        stage.showAndWait();

        CreateNewAlbum controller = loader.getController();
        String s = controller.getResponse();
    }

    @FXML
    private void viewAlbum(ActionEvent ae) throws IOException {
        getGalleryView(ae);
    }

    @FXML
    public void getGalleryView(ActionEvent ae) throws IOException {
//        String marked = albumList.getSelectionModel().getSelectedItem();
//        if (marked == null) {
//            //TODO add error txt field as member.
//            TextField textField = new TextField();
//            textField.setText("You must select an album to show");
//            return;
//        }
//        HBox hb = new HBox();
//        this.selectedAlbum = marked;
//        ViewAlbums va = new ViewAlbums();
//        va.getAlbum(this.selectedAlbum);
//        List<Image> images = new ArrayList<>(2);
//        ImageView[] pics = new ImageView[2/*albumImages.size()*/];
//        List<CTImage> albumImages = va.getImages();
//
//        while (albumImages.iterator().hasNext()) {
//            ByteArrayInputStream bais = new ByteArrayInputStream(albumImages.iterator().next().getImageData());
//            BufferedImage image = ImageIO.read(bais);
//            images.add(SwingFXUtils.toFXImage(image, null));
//        }
//        final String[] imageNames = new String[]{"2.png", "cam.png"};
//        for (int i = 0; i < 2/*albumImages.size()*/; i++) {
//            pics[i] = new ImageView(images.get(i));
//            pics[i].setFitWidth(100);
//            pics[i].setPreserveRatio(true);
//            hb.getChildren().add(pics[i]);
//            this.pics.setContent(hb);
//
//        }
        Parent root = FXMLLoader.load(getClass().getResource("AlbumViewC.fxml"));
        Stage stageTheLabelBelongs = (Stage) viewAlbums.getScene().getWindow();
        stageTheLabelBelongs.setMaxWidth(600);
        stageTheLabelBelongs.setMaxHeight(400);
        Scene scene = new Scene(root,600,400);
        ((Pane) scene.getRoot()).getChildren().add(this.pics);
        Button back = new Button("Back");
        back.setAlignment(Pos.BOTTOM_CENTER);
        ((Pane) scene.getRoot()).getChildren().add(back);
        stageTheLabelBelongs.setScene(scene);
        stageTheLabelBelongs.show();
    }
}

