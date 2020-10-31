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

import Message.*;
public class Pgcreate extends Login {
    public Button backBT;
    public Button startBT;
    public TextArea musicTA;
    public Button doneBT;
    public Button privateBT;
    public Button listBT;
    public static String s[]=new String[50];
    public static int i=0;
    public TextField nameTF;
    public TextField musicTF;
    public Socket socket;
    public static int pri=0;
    ObjectOutputStream op;
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
        stage.setScene(new Scene(root,400, 600));
    }
    //Listener function of Button make to private
    public void lbtprivate(ActionEvent actionEvent) {
        pri = 1;
    }
    //Listener function of Button start
    public void lbts(ActionEvent actionEvent){
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

    //Listener function of Button done
    public void lbtd(ActionEvent actionEvent){
        System.out.println("Create");
        ObjectOutputStream o = this.op;
        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    o.writeObject(new Message_Plalist(nameTF.getText(),s,i,pri));
                    o.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }
    //Listener function of Button enter name of play list
    public void lbtl(ActionEvent actionEvent)throws Exception {
        socket = new Socket("127.0.0.1", 5402);
        op = new ObjectOutputStream(socket.getOutputStream());
        op.writeObject(new Message_Music(getUserName(),1));
    }
    //Listener function of Button enter Music
    public void lbtm(ActionEvent actionEvent)throws Exception {
        s[i]=musicTF.getText();
        i++;
    }
}
