package sample;


import javax.sound.sampled.*;

import Music_play.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import File.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class PgOfline {
    public Button playBT;
    public Button musicBT;
    public Button subBT;
    public Label subLB;
    public static String musicf;
    public static String subf;
    public Button backBT;
    public void lbtm(ActionEvent actionEvent){
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(null);
        if(f!=null){
            musicf = f.getAbsolutePath();
        }
    }

    public void lbts(ActionEvent actionEvent){
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(null);
        if(f!=null){
            subf = f.getAbsolutePath();
        }
    }

    public void lbtp(ActionEvent actionEvent){
        System.out.println("starting");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String a = musicf;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String b = subf;
                        File subfile = FileUtil.getFile(b);
                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(subfile)));
                            String line;
                            while ((line = in.readLine()) != null)
                            {
                                String a[] = new String [4];
                                for(int i=0;i<3;i++){
                                    a[i] = line;
                                    line = in.readLine();
                                }
                                a[3] = line;
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        subLB.setText(a[2]);
                                    }
                                });
                                int t1 = Integer.parseInt(a[1].substring(6,8))+Integer.parseInt(a[1].substring(3,5))*60;
                                int t2 = Integer.parseInt(a[1].substring(23,25))+Integer.parseInt(a[1].substring(20,22))*60;
                                Thread.sleep((t2-t1)*1000+300);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Mp3Player m = new Mp3Player(a);
                m.play();

            }
            private synchronized void play(final BufferedInputStream in) throws Exception {
                AudioInputStream ais = AudioSystem.getAudioInputStream(in);
                try (Clip clip = AudioSystem.getClip()) {
                    clip.open(ais);
                    clip.start();
                    Thread.sleep(100); // given clip.drain a chance to start
                    clip.drain();
                }
            }
        }).start();
    }
    public void lbtb(ActionEvent actionEvent){
        System.out.println("BACK");
        Parent root=null;
        Stage stage = (Stage) backBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./pg1.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,600, 400));
    }
}
