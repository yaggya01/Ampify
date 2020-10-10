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
public class HandleClient implements Runnable{
    final private Socket socket;
    ObjectInputStream oi;
    ObjectOutputStream op;
    public HandleClient(Socket s,ObjectOutputStream o)
    {
        this.socket = s;
        op = o;
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
                Message m = (Message) oi.readObject();
                System.out.println(m);
                String url = "jdbc:mysql://localhost:3306/school";
                Connection connection = DriverManager.getConnection(url,"root","root");
                String q = "Select Location from MUSIC_SERVER where Name=";
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
                    args[0]=result.getString("Location");
                    if (args.length == 0)
                        throw new IllegalArgumentException("expected sound file arg");
                    File soundFile = AudioUtil.getSoundFile(args[0]);
                    System.out.println("server: " + soundFile);

                    try (FileInputStream in = new FileInputStream(soundFile)) {
                        System.out.println(args[0]);
                        Socket client = socket;
                        System.out.println("CLIENT CONNECTED");
                        OutputStream out = client.getOutputStream();

                        byte buffer[] = new byte[2048];
                        int count;
                        play(new BufferedInputStream(in));
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
    private static synchronized void play(final BufferedInputStream in) throws Exception {
        AudioInputStream ais = AudioSystem.getAudioInputStream(in);
        try (Clip clip = AudioSystem.getClip()) {
            clip.open(ais);
            clip.start();
            Thread.sleep(100); // given clip.drain a chance to start
            clip.drain();
        }
    }
}