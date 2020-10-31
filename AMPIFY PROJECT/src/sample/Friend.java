package sample;

import Message.*;
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

import java.io.*;
import java.net.Socket;

public class Friend extends Pg1 {
    public Button userBT;
    public Button backBT;
    public TextField userTF;
    public TextField acceptTF;
    public Label userLB;
    public TextArea requestTA;
    public TextArea friendTA;
    public Button startBT;
    public Button acceptBT;
    public void lbta(ActionEvent actionEvent)throws Exception{
        String a = acceptTF.getText();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {Socket socket = new Socket("localhost",5400);
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    sendMessage(socket,new BufferedReader(new InputStreamReader(System.in)),getUserName(),a,op);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            public void sendMessage(Socket socket, BufferedReader in, String name, String user, ObjectOutputStream op)throws IOException{
                op.writeObject(new Message(name,user,null,0,4));
                op.flush();
                System.out.println("Client closed");
            }
        }).start();
    }
    public void lbtb(ActionEvent actionEvent)throws Exception{
        System.out.println("All Songs");
        Parent root=null;
        Stage stage = (Stage) backBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./pg1.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,600, 800));
    }
    public void lbtu(ActionEvent actionEvent)throws Exception{
        String a = userTF.getText();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("TRY");
                    Socket socket = new Socket("localhost",5400);
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    sendMessage(socket,new BufferedReader(new InputStreamReader(System.in)),a,getUserName(),op);
                    ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
                    Message_Server m = (Message_Server) oi.readObject();
                    String a = m.n;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            userLB.setText(a);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            public void sendMessage(Socket socket, BufferedReader in, String name, String user, ObjectOutputStream op)throws IOException{
                op.writeObject(new Message(name,user,null,0,1));
                op.flush();
                System.out.println("Client closed");
            }
        }).start();
    }
    public void lbts(ActionEvent actionEvent) throws Exception {
        Socket socket = new Socket("localhost",5400);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    op.writeObject(new Message(getUserName(),null,null,0,2));
                    op.flush();
                    System.out.println("Client closed");
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();
        new Thread(() -> {
            try{
                while(true){
                    ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
                    Message_Request m = (Message_Request) oi.readObject();
                    if(m!=null) {
                        System.out.println("RECEIVED");
                        for (int j = 0; j < m.k; j++) {
                            requestTA.appendText(m.s[j] + "\n");
                        }
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }).start();

        //
       Thread.sleep(1000);
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
}
