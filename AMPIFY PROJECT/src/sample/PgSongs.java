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

public class PgSongs {
    public Button playBT;
    public TextArea musicTA;
    public TextField musicTF;
    public Button startBT;
    public Button backBT;
    public Button editBT;
    public Label subLB;
    public static Deque<String> deque = new LinkedList<String>();
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
    }

    public void lbtp(ActionEvent actionEvent)throws Exception {
        System.out.println("PLAYING");

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("1");
                try {

                    final Socket socket = new Socket("127.0.0.1", 5402);
                    // play soundfile from server
                    if (socket.isConnected()) {
                        System.out.println("ENTER Name of song");
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        String c = musicTF.getText();
                        ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                        sendMessage(socket,br,c,op);
                        System.out.println("Client: reading from 127.0.0.1:5400");

                        InputStream l = socket.getInputStream();
                        //

                        Thread t = new Thread(new Song(c));
                        t.start();
                        Thread.sleep(3000);
                        try{
                            BufferedReader in1 = new BufferedReader(new InputStreamReader(l, "UTF-8"));
                            System.out.println(in1);
                            String line;
                            while ((line = in1.readLine()) != null)
                            {
                                String a[] = new String [4];
                                for(int i=0;i<3;i++){
                                    a[i] = line;
                                    line = in1.readLine();
                                }
                                a[3] = line;
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        subLB.setText(a[2]);
                                    }
                                });
                                int t1 = Integer.parseInt(a[1].substring(6,8))+Integer.parseInt(a[1].substring(3,5))*60;
                                int t2 = Integer.parseInt(a[1].substring(23,25))+Integer.parseInt(a[1].substring(20,22))*60;
                                int t3 = Integer.parseInt(a[1].substring(26,29))-Integer.parseInt(a[1].substring(9,12));
                                Thread.sleep((t2-t1)*1000+t3);
                            }}
                        catch (Exception e){
                            e.printStackTrace();
                        }


                        //
                    }

                    System.out.println("Client: end");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            public void sendMessage(Socket socket, BufferedReader in, String name,ObjectOutputStream op)throws IOException {
                op.writeObject(new Message_Music(name,2));
                op.flush();
                return;
            }


        }).start();

        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    String url = "jdbc:mysql://localhost:3306/Local";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Insert into HISTORY VALUES(?,?,?)";
                    PreparedStatement preSat = connection.prepareStatement(q);
                    preSat.setString(1, musicTF.getText());
                    preSat.setString(2, String.valueOf(java.time.LocalDate.now()));
                    preSat.setString(3, String.valueOf(java.time.LocalTime.now()));
                    preSat.execute();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


    }
    public void lbte(ActionEvent actionEvent){

    }

}
