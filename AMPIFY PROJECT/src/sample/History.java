package sample;

import Message.Message_History;
import Message.Message_Music;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class History extends Login{
    public TextArea historTA;
    public Button startBT;
    public Button backBT;
    //Listener function of Button back
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
    //Listener function of Button start get history
    public void lbts1(ActionEvent actionEvent) throws Exception {
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
                    for(int j=0;j< m.k;j++){
                        historTA.appendText(m.s[j]+"\n");
                    }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
