package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.Socket;
import java.sql.*;
import java.io.*;

import java.io.IOException;
import java.sql.SQLException;

public class Singup {
    public Button sBT;
    public TextField userTF;
    public TextField passTF;
    public Label lable;
    public void ListenerSbt(ActionEvent actionEvent)
    {
       System.out.println("UserName: "+userTF.getText()+"\nPassword: "+passTF.getText());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader in = new BufferedReader (new InputStreamReader(System.in));
                    Socket socket = new Socket("localhost",5400);//ip adress and port
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lable.setText("Client Started");
                        }
                    });
                    String name = userTF.getText();
                    String password = passTF.getText();
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    sendMessage(socket,in,name,password,op);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lable.setText("Done");
                        }
                    });
                }catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            public void sendMessage(Socket socket, BufferedReader in, String name, String password, ObjectOutputStream op)throws IOException{
                op.writeObject(new Message(name,password,1));
                op.flush();
                System.out.println("Client closed");
            }
        }).start();
    }
}
