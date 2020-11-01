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

public class Pg1 extends Login {
    public Button songBT;
    public Button oflineBT;
    public Button createBT;
    public Button playBT;
    public Button historyBT;
    public Button editBT;
    public Button groupBT;
    public Button downloadBT;
    public Button logoutBT;
    public Button friendBT;
    public Button videoBT;
    public Button friendPlaylistBT;
    public Button localBT;

    //Listener function of Button download
    public void ListenerDBT(ActionEvent actionEvent){
        System.out.println("All Songs");
        Parent root=null;
        Stage stage = (Stage) songBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./download.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,600, 400));
    }

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
        stage.setScene(new Scene(root,600, 400));
    }
    //Listener function of Button ofline songs
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
        stage.setScene(new Scene(root,600, 400));
    }
    //Listener function of Button create play list
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
        stage.setScene(new Scene(root,600, 400));
    }
    //Listener function of Button play a playlist
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
        stage.setScene(new Scene(root,600, 400));
    }
    //Listener function of Button history
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
        stage.setScene(new Scene(root,600, 400));
    }
    //Listener function of Button edit playlist
    public void Listeneredit(ActionEvent actionEvent){
        System.out.println("Edit Playlist");
        Parent root=null;
        Stage stage = (Stage) oflineBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./editPL.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,600, 400));
    }
    //Listener function of Button logout
    public void lbtlo(ActionEvent actionEvent) throws Exception {
        getSocket().close();
        Stage stage = (Stage) logoutBT.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    //Listener function of Button friends
    public void lbtf(ActionEvent actionEvent) throws Exception {
        Parent root=null;
        Stage stage = (Stage) friendBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./friend.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,400, 200));
    }
    //Listener function of Button friends playlist
    public void lbtfpl(ActionEvent actionEvent){
        Parent root=null;
        Stage stage = (Stage) friendPlaylistBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./friendPL.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,600, 400));
    }
    //Listener function of Button group functions
    public void lbtg(ActionEvent actionEvent) throws Exception {
        Parent root=null;
        Stage stage = (Stage) friendPlaylistBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./group.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,600, 400));
    }
    //Listener function of Button videos
    public void lbtv(ActionEvent actionEvent) {
        Parent root=null;
        Stage stage = (Stage) videoBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./video.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,600, 400));
    }

    public void lbtlocal(ActionEvent actionEvent) {
        Parent root=null;
        Stage stage = (Stage) videoBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./songshare.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,600, 400));
    }
}

