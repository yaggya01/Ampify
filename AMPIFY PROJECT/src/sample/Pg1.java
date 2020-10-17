package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;

public class Pg1 {
    public Button songBT;
    public Button oflineBT;
    public Button createBT;
    public Button playBT;
    public Button historyBT;
    public void ListenerSongBT(ActionEvent actionEvent){
        System.out.println("All Songs");
        Parent root=null;
        Stage stage = (Stage) songBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./pgSongs.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,300, 275));
    }

    public void ListenerOfBT(ActionEvent actionEvent){
        System.out.println("Offline Songs");
        Parent root=null;
        Stage stage = (Stage) oflineBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./pgOfline.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,300, 275));
    }

    public void ListenerCBT(ActionEvent actionEvent) {
        System.out.println("Offline Songs");
        Parent root=null;
        Stage stage = (Stage) oflineBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./pgcreate.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,300, 275));
    }

    public void ListenerPBT(ActionEvent actionEvent) {
        System.out.println("Offline Songs");
        Parent root=null;
        Stage stage = (Stage) oflineBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./playPL.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,300, 275));
    }

    public void lbth(ActionEvent actionEvent){
        System.out.println("Offline Songs");
        Parent root=null;
        Stage stage = (Stage) oflineBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./history.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,300, 275));
    }
}

