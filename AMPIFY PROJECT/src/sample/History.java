package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class History {
    public TextArea historTA;
    public Button startBT;
    public void lbts(ActionEvent actionEvent){
        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    String url = "jdbc:mysql://localhost:3306/Local";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Select * from History";
                    PreparedStatement preSat = connection.prepareStatement(q);
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    while (true) {
                        if (!result.next()) break;
                        String regno = result.getString("Song");
                        String date = result.getString("Date");
                        String time = result.getString("Time");
                        historTA.appendText(regno + " " + date +" " +time+ "\n");
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
