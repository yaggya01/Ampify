package sample;

import Message.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Signselect extends Signup {
    public Button languageBT;
    public Button artistBT;
    public Button doneBT;
    public Button gernersBT;
    public Button languageTF;
    public Button artistTF;
    public Button gernersTF;
    public static String language;
    public static String artist;
    public static String gerners;
    //Listener function of Button language
    public void lbtl(ActionEvent actionEvent) {
        language = languageTF.getText();
    }
    //Listener function of Button artist
    public void lbta(ActionEvent actionEvent) {
        artist = artistTF.getText();
    }
    //Listener function of Button gerners
    public void lbtg(ActionEvent actionEvent) {
        gerners = gernersTF.getText();
    }
    //Listener function of Button done
    public void lbtd(ActionEvent actionEvent) throws Exception {
        Socket socket = new Socket("localhost",5400);
        ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
        op.writeObject(new Message(getname(),language,artist+'_'+gerners,0,10));
        op.flush();
        op.close();
        System.out.println("BACK");
        Parent root=null;
        Stage stage = (Stage) signBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./sample.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,300, 275));
    }
}
