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

public class Login {
    public Button lBT;
    public TextField userTF;
    public TextField passTF;
    public Label lable;
    public void lBTListener(ActionEvent actionEvent)
    {
        System.out.println("USERNAME: "+userTF.getText()+"\nPASSWORD: "+passTF.getText());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Socket socket = new Socket("localhost",5400);//ip adress and port
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lable.setText("Client Started");
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
                                                lable.setText(k.n);
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
                op.writeObject(new Message(name,password,0));
                op.flush();
                System.out.println("Client closed");
            }
        }).start();
    }
}
