package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Deque;
import java.util.LinkedList;
import Music_play.*;
import Message.*;

public class PgSongs extends Login {
    public Button playBT;
    public TextArea musicTA;
    public TextField musicTF;
    public Button likeBT;
    public Button startBT;
    public Button backBT;
    public Button editBT;
    public Button nextBT;
    public Button queueBT;
    public TextArea queueTA;
    public Label mpLB;
    public TextArea rpTA;
    public static String song;
    public static BufferedInputStream in;
    public static BufferedReader in1;
    public static Deque<String> deque = new LinkedList<String>();
    //Listener function of Button next button
    public void lbtn(ActionEvent actionEvent) throws Exception {
        song = deque.getFirst();
        String a = this.getUserName();
        deque.pop();
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
            root = FXMLLoader.load(getClass().getResource("./sound.fxml"));
        } catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,400, 200));
        stage.show();

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
        stage.setScene(new Scene(root,400, 1000));
    }
    //Listener function of Button like
    public void lbtl(ActionEvent actionEvent)throws Exception {
        System.out.println("Like Add");
        final Socket socket = new Socket("127.0.0.1", 5402);
        ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
        op.writeObject(new Message_Music(getUserName(),7));
        op.flush();
        op.writeObject(new Message_Music(musicTF.getText(),0));
    }
    //Listener function of Button start
    public void lbts(ActionEvent actionEvent) throws InterruptedException, IOException {
        System.out.println("starting");

        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = null;
                    connection = DriverManager.getConnection(url, "root", "root");

                    String q = "Select * from MUSIC_USER;";
                    PreparedStatement preSat;
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    while (true) {
                        if (!result.next()) break;
                        int regno = result.getInt("Sr");
                        String name = result.getString("Name");
                        musicTA.appendText(Integer.toString(regno) + " " + name + "\n");
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("127.0.0.1", 5402);
                    ObjectOutputStream op= new ObjectOutputStream(socket.getOutputStream());
                    op.writeObject(new Message_Music(getUserName(),10));
                    op.flush();
                    ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
                    Message_Music m = (Message_Music) oi.readObject();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            mpLB.setText(m.name);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        final Socket socket = new Socket("127.0.0.1", 5402);
        String a = getUserName();
        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    op.writeObject(new Message_Music(a,4));
                    op.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        Thread.sleep(500);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
                    while(true){
                        Message_History m = (Message_History) oi.readObject();
                        for(int j=0;j<4&&j<m.k;j++){
                            rpTA.appendText(m.s[j]+"\n");
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        final int[] f = {0};
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(deque.size()!= f[0]){
                        queueTA.clear();
                        f[0] = deque.size();
                        String[] a=new String[f[0]];
                        int i=0;
                        while(!deque.isEmpty()){
                            a[i]=deque.getFirst();
                            i++;
                            queueTA.appendText(deque.getFirst()+"\n");
                            deque.pop();

                        }
                        for(int j=0;j<i;j++){
                            deque.addLast(a[j]);
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    //Listener function of Button play songs
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
            root = FXMLLoader.load(getClass().getResource("./sound.fxml"));
        } catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,400, 200));
        stage.setTitle(song);
        stage.show();



    }
    //Listener function of Buttons enter queue
    public void lbtaq(ActionEvent actionEvent){
        deque.addFirst(musicTF.getText());
    }
    public void lbtaq2(ActionEvent actionEvent){
        deque.addLast(musicTF.getText());
    }
    public String getName(){
        return song;
    }

}
