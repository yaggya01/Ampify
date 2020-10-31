package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Message.*;
public class Group extends Pg1{
    public Button startBT;
    public Button backBT;
    public Button enterBT;
    public Button playlistBT;
    public Button playBT;
    public TextField nameTF;
    public TextField songTF;
    public TextField playlistTF;
    public TextArea groupnameTA;
    public TextArea playlistTA;
    public TextArea songTA;
    public Button cgroupBT;
    public Button creatplBT;
    public static String playlist[];
    public static String song;
    public static String groupname;

    public void lbtb(ActionEvent actionEvent) throws Exception {
        System.out.println("All Songs");
        Parent root=null;
        Stage stage = (Stage) backBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./pg1.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,600, 700));
    }
    public void lbtc(ActionEvent actionEvent) throws Exception {
        System.out.println("All Songs");
        Parent root=null;
        Stage stage = (Stage) backBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./groupcreate.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,600, 700));
    }
    public void lbts(ActionEvent actionEvent) throws Exception {
        Socket socket = new Socket("localhost",5400);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    op.writeObject(new Message(getUserName(),null,null,0,5));
                    op.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
                    while(true){
                        Message_Request m = (Message_Request) oi.readObject();
                        for(int j=0;j< m.k;j++){
                            groupnameTA.appendText(m.s[j]+"\n");
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public  static String getgroupName(){
        System.out.println(groupname);
        return groupname;
    }
    public void lbte(ActionEvent actionEvent) throws Exception {

        Socket socket = new Socket("localhost",5400);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());

                    System.out.println(nameTF.getText());
                    op.writeObject(new Message(getUserName(),nameTF.getText(),null,0,6));
                    op.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
                    while(true){
                        Message_Request m = (Message_Request) oi.readObject();
                        for(int j=0;j< m.k;j++){
                            String a="";int k=0;
                            while(m.s[j].charAt(k)!='_'){
                                a= a+m.s[j].charAt(k);
                                k++;
                            }
                            playlistTA.appendText(a+"\n");
                        }
                        playlist = new String[m.k];
                        for(int j=0;j< m.k;j++){
                            playlist[j]=m.s[j];
                        }

                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void lbtplaylist(ActionEvent actionEvent) throws Exception {
        Socket socket = new Socket("localhost",5400);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String a="";

                    for(int i=0;i<playlist.length;i++){
                        String c="";int k=0;
                        while(playlist[i].charAt(k)!='_'){
                            c = c+ playlist[i].charAt(k);
                            k++;
                        }
                        if(c.equals(playlistTF.getText())){
                            a = playlist[i];
                            break;
                        }
                    }
                    System.out.println(a);
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    op.writeObject(new Message(a,null,null,0,7));
                    op.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
                    while(true){
                        Message_Request m = (Message_Request) oi.readObject();
                        for(int j=0;j< m.k;j++){
                            songTA.appendText(m.s[j]+"\n");
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public String getName(){
        return song;
    }
    public void lbtp(ActionEvent actionEvent)throws Exception {
        System.out.println("PLAYING");
        song = songTF.getText();
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
                    s[0]=songTF.getText()+';'+java.time.LocalDate.now()+';'+java.time.LocalTime.now();
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
            root = FXMLLoader.load(getClass().getResource("./sound6.fxml"));
        } catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,400, 200));
        stage.show();
    }

    public void lbtcreate(ActionEvent actionEvent) {
        groupname = nameTF.getText();
        System.out.println(groupname);
        Parent root=null;
        Stage stage = (Stage) creatplBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./creategroupplaylis.fxml"));
        } catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,400, 200));
        stage.show();
    }
}
