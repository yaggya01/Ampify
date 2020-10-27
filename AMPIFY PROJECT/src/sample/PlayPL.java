package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Music_play.*;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.StringTokenizer;
import Message.*;

public class PlayPL extends Login{
    public Button startBT;
    public Button backBT;
    public Button playBT;
    public Button nameBT;
    public Label subLB;
    public TextField musicTF;
    public TextField listTF;
    public TextArea musicTA;
    public TextArea listTA;
    public Socket socket;
    public ObjectOutputStream op;
    public ObjectInputStream oi;
    public static BufferedReader in1;
    public static BufferedInputStream in;
    public static String song;
    public PlayPL() throws IOException {
        socket = new Socket("localhost",5402);
    }

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
        stage.setScene(new Scene(root,300, 275));
    }

    public void lbtp(ActionEvent actionEvent)throws Exception {
        System.out.println("PLAYING");
        song = musicTF.getText();
        String a = this.getUserName();
        System.out.println("PLAYER");
        Socket socket = new Socket("localHost",5402);
        ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
        Thread.sleep(500);
        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    String s[] = new String[1];
                    s[0]=musicTF.getText()+';'+java.time.LocalDate.now()+';'+java.time.LocalTime.now();
                    op.writeObject(new Message_Music(a,3));
                    op.flush();
                    op.writeObject(new Message_History(s,1));
                    op.flush();
                    System.out.println("TFVC");
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        Parent root=null;
        Stage stage = (Stage) new Stage();
        try{
            root = FXMLLoader.load(getClass().getResource("./sound2.fxml"));
        } catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,400, 200));
        stage.show();



    }
    public String getName(){
        return song;
    }
    public void lbtn(ActionEvent actionEvent)throws Exception {
        String a = listTF.getText();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    op.writeObject(new Message_Music(a,5));
                    op.flush();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message_Plalist m = (Message_Plalist) oi.readObject();
                    for(int i=0;i<m.k;i++) {
                        musicTA.appendText(m.s[i] + "\n");
                        System.out.println("i");
                    }
                    oi.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public void lbts(ActionEvent actionEvent) throws Exception {
        String a = getUserName();
        System.out.println("i");
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    op= new ObjectOutputStream(socket.getOutputStream());
                    op.writeObject(new Message_Music(a,5));
                    op.flush();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    oi=new ObjectInputStream(socket.getInputStream());
                    Message_Plalist m = (Message_Plalist) oi.readObject();
                            for(int i=0;i<m.k;i++) {
                                listTA.appendText(m.s[i] + "\n");
                                System.out.println("i");
                            }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
