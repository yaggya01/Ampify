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
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Pgcreate extends Login {
    public Button backBT;
    public Button startBT;
    public TextArea musicTA;
    public Button doneBT;
    public Button listBT;
    public static String s="";
    public TextField nameTF;
    public TextField musicTF;
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

    public void lbts(ActionEvent actionEvent){
        s = this.getUserName()+";";
        System.out.println(s);
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
    }


    public void lbtd(ActionEvent actionEvent){
        System.out.println("Create");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Socket socket = new Socket("127.0.0.1", 5402);
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    sendMessage(socket, s, op);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            public void sendMessage(Socket socket, String name, ObjectOutputStream op)throws IOException{
                op.writeObject(new Message_Music(name,1));
                op.flush();
                return;
            }
        }).start();

    }

    public void lbtl(ActionEvent actionEvent)throws Exception {
        s = s+nameTF.getText()+";";
    }
    public void lbtm(ActionEvent actionEvent)throws Exception {
        s = s+musicTF.getText()+";";
    }
}
