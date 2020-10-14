package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.io.*;
import java.net.Socket;
public class HandleClient_Subtitle implements Runnable{
    final private Socket socket;
    ObjectInputStream oi;
    public HandleClient_Subtitle(Socket s)
    {
        this.socket = s;
        try{
            oi = new ObjectInputStream(socket.getInputStream());
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        while(true){
            try{
                Message_Music m = (Message_Music) oi.readObject();
                System.out.println(m);
                String url = "jdbc:mysql://localhost:3306/school";
                Connection connection = DriverManager.getConnection(url,"root","root");
                String q = "Select * from MUSIC_SERVER where Name=";
                q = q+'"';
                q = q+m.name;
                q = q +'"';
                q = q+';';
                System.out.println(q);
                PreparedStatement preSat;
                preSat = connection.prepareStatement(q);
                ResultSet result = preSat.executeQuery();
                if(result.next()) {
                    String args[] = new String [100];
                    args[1]=result.getString("Location2");
                    if (args.length == 0)
                        throw new IllegalArgumentException("expected sound file arg");
                    File soundFile1 = FileUtil.getFile(args[1]);
                    System.out.println("server: " + soundFile1);

                    try (FileInputStream in1 = new FileInputStream(soundFile1)) {
                        Socket client = this.socket;
                        System.out.println(args[1]);
                        System.out.println("CLIENT CONNECTED");
                        OutputStream out = client.getOutputStream();


                        byte buffer1[] = new byte[2048];
                        int count1;
                        while ((count1 = in1.read(buffer1)) != -1)
                            out.write(buffer1, 0, count1);


                    }

                    System.out.println("server: shutdown");
                }
            }

            catch(Exception e){
                e.printStackTrace();
                return;
            }
        }
    }
}