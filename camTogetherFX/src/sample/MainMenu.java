package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 *
 */
public class MainMenu {
    @FXML
    private Button Connect;
    @FXML
    private Button Disconnect;
    @FXML
    private Button viewAlbums;


    @FXML
    void Connect() {
        Stage stage = (Stage) Connect.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenuC.fxml"));
        try {

            CTClient client = new CTClient();
            client.createConnection();
            HBox root = (HBox) loader.load();
            loader.setController(this);
            Scene menu = new Scene(root, 600, 400);
            stage.setScene(menu);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
