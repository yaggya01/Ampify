package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.io.*;
import java.net.Socket;
public class HandleClient_Music implements Runnable{
    final private Socket socket;
    ObjectInputStream oi;
    public HandleClient_Music(Socket s)
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
                    args[0]=result.getString("Location1");
                    if (args.length == 0)
                        throw new IllegalArgumentException("expected sound file arg");
                    File soundFile = AudioUtil.getSoundFile(args[0]);
                    System.out.println("server: " + soundFile);

                    try (FileInputStream in = new FileInputStream(soundFile)) {
                        Socket client = this.socket;
                        System.out.println(args[0]);
                        System.out.println("CLIENT CONNECTED");
                        OutputStream out = client.getOutputStream();


                        byte buffer[] = new byte[2048];
                        int count;
                        while ((count = in.read(buffer)) != -1)
                            out.write(buffer, 0, count);


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