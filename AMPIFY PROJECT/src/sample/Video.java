package sample;


import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
public class Video
{

    public static String musicf;
    public Button backBT;
    public Button playBT;
    //Listener function of Button play
    public void lbtp(ActionEvent actionEvent) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = musicf;

                //Instantiating Media class
                Media media = new Media(new File(path).toURI().toString());

                //Instantiating MediaPlayer class
                MediaPlayer mediaPlayer = new MediaPlayer(media);

                //Instantiating MediaView class
                MediaView mediaView = new MediaView(mediaPlayer);

                //by setting this property to true, the Video will be played
                mediaPlayer.setAutoPlay(true);

                //setting group and scene
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Group root = new Group();
                        root.getChildren().add(mediaView);
                        Scene scene = new Scene(root,500,400);
                        Stage primaryStage =(Stage) new Stage();
                        primaryStage.setScene(scene);
                        primaryStage.setTitle("Playing video");
                        primaryStage.show();
                    }
                });

            }
        }).start();


    }
    //Listener function of Button find file
    public void lbtm(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(null);
        if(f!=null){
            musicf = f.getAbsolutePath();
        }
    }
    //Listener function of Button back
    public void lbtb(ActionEvent actionEvent){
        System.out.println("All Songs");
        Parent root=null;
        Stage stage = (Stage) backBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./pg1.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,400, 600));
    }
}