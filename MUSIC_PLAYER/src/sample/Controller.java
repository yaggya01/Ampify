package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.*;
import java.sql.*;
import javax.sound.sampled.*;
public class Controller {
    public Button playBT;
    public Button startBT;
    public TextArea musicTA;
    public TextField musicTF;
    public void lbts(ActionEvent actionEvent){
        System.out.println("starting");

        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    String url = "jdbc:mysql://localhost:3306/school";
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
                try {

                    // play soundfile from server
                    try (Socket socket = new Socket("127.0.0.1", 5400)) {
                        if (socket.isConnected()) {
                            System.out.println("ENTER Name of song");
                            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                            String c = musicTF.getText();
                            ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                            sendMessage(socket,br,c,op);
                            System.out.println("Client: reading from 127.0.0.1:6666");
                            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
                            System.out.println(in);
                            play(in);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Client: end");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            public void sendMessage(Socket socket, BufferedReader in, String name,ObjectOutputStream op)throws IOException{
                op.writeObject(new Message(name));
                op.flush();
                return;
            }
            private synchronized void play(final BufferedInputStream in) throws Exception {
                AudioInputStream ais = AudioSystem.getAudioInputStream(in);
                try (Clip clip = AudioSystem.getClip()) {
                    clip.open(ais);
                    clip.start();

                    Thread.sleep(100); // given clip.drain a chance to start
                    clip.drain();
                }
            }

        }).start();
        }
    }