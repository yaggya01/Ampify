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
import Message.*;

public class Login {
    public Button backBT;
    public Button loginBT;
    public TextField userTF;
    public TextField passTF;
    public Label label;
    public static String User;
    private static Socket socket;
    //Listener function of Button back
    public void ListenerbBT(ActionEvent actionEvent){

        System.out.println("BACK");
        Parent root=null;
        Stage stage = (Stage) backBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./sample.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,300, 275));
    }
    //Listener function of Button login
    public void ListenerlBT(ActionEvent actionEvent){
        System.out.println("USERNAME: "+userTF.getText()+"\nPASSWORD: "+passTF.getText());
        User = userTF.getText();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long i =0;
                while(i<24*60*60){
                    i++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Stage stage = (Stage) loginBT.getScene().getWindow();
                        // do what you have to do
                        stage.close();
                    }
                });
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket = new Socket("localhost",5400);//ip adress and port
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            label.setText("Client Started");
                        }
                    });
                    String name = userTF.getText();
                    String password = passTF.getText();
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ObjectInputStream oi=null;
                            try{
                                oi = new ObjectInputStream(socket.getInputStream());
                            }
                            catch(IOException e){
                                e.printStackTrace();
                            }
                            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                            try{
                                System.out.println("Message Recived: ");
                                while(true)
                                {
                                    Message_Server k = (Message_Server) oi.readObject();
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            label.setText(k.n);
                                            if(k.n.equals("LOGED IN"))
                                            {
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
                                    });
                                }
                            }
                            catch(Exception e){
                                e.printStackTrace();
                                return;
                            }

                        }
                    }).start();
                    sendMessage(socket,in,name,password,op);
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            public void sendMessage(Socket socket, BufferedReader in, String name,String password,ObjectOutputStream op)throws IOException{
                op.writeObject(new Message(name,password,null,0,0));
                op.flush();
                System.out.println("Client closed");
            }
        }).start();
    }
    public String getUserName(){
        return User;
    }
    public Socket getSocket(){
        return socket;
    }

}
