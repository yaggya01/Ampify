package sample;

import Message.*;
import Message.Message_Request;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class FriendPL extends Pg1{
    public Button startBT;
    public Button backBT;
    public Button playBT;
    public Button enterBT;
    public Button nameBT;
    public TextArea friendTA;
    public TextArea listTA;
    public TextArea songTA;
    public TextField songTF;
    public TextField nameTF;
    public TextField enterTF;
    public static String song;
    public void lbts(ActionEvent actionEvent) throws IOException {
        Socket socket1 = new Socket("localhost",5400);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    ObjectOutputStream op1 = new ObjectOutputStream(socket1.getOutputStream());
                    sendMessage(socket1,new BufferedReader(new InputStreamReader(System.in)),getUserName(),op1);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            public void sendMessage(Socket socket, BufferedReader in, String name, ObjectOutputStream op)throws IOException{
                op.writeObject(new Message(name,null,null,0,3));
                op.flush();
                System.out.println("Client closed");
            }
        }).start();
        new Thread(() -> {
            try{
                while(true){
                    ObjectInputStream oi1 = new ObjectInputStream(socket1.getInputStream());
                    Message_Request m = (Message_Request) oi1.readObject();
                    for(int j=0;j< m.k;j++){
                        friendTA.appendText(m.s[j]+"\n");
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }).start();
    }
    public void lbte(ActionEvent actionEvent) throws IOException {
        String a = enterTF.getText();
        System.out.println("i");
        Socket socket = new Socket("localhost",5402);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    ObjectOutputStream op ;
                    op= new ObjectOutputStream(socket.getOutputStream());
                    op.writeObject(new Message_Music(a,12));
                    op.flush();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();
        new Thread(() -> {
            try {
                ObjectInputStream oi;
                oi=new ObjectInputStream(socket.getInputStream());
                Message_Plalist m = (Message_Plalist) oi.readObject();
                for(int i=0;i<m.k;i++) {
                    listTA.appendText(m.s[i] + "\n");
                    System.out.println("i");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }
    public void lbtp(ActionEvent actionEvent) throws IOException, InterruptedException {
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
            root = FXMLLoader.load(getClass().getResource("./sound5.fxml"));
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
        String a = enterTF.getText();
        Socket socket = new Socket("localhost",5402);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    op.writeObject(new Message_Music(a,13));
                    op.writeObject(new Message_Music(nameTF.getText(),0));
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
                    ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
                    Message_Plalist m = (Message_Plalist) oi.readObject();
                    for(int i=0;i<m.k;i++) {
                        songTA.appendText(m.s[i] + "\n");
                        System.out.println("i");
                    }
                    oi.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
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
}
