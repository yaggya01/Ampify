package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    public Button loginBT;
    public Button signBT;
    public void loginBTListener(ActionEvent actionEvent)
    {   Parent root=null;
        System.out.println("LOGIN");
        Stage stage = (Stage) loginBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./login.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,300, 275));
    }
    public void signBTListener(ActionEvent actionEvent)
    {
        System.out.println("Sign up");
    }
}
