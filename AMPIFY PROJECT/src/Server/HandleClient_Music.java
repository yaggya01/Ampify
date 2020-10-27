package Server;
import File.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

import Message.*;
import Music_play.Song;

public class HandleClient_Music implements Runnable{
    final private Socket socket;
    ObjectInputStream oi;
    public HandleClient_Music(Socket s) throws IOException {
        this.socket = s;
        try{
            oi = new ObjectInputStream(socket.getInputStream());
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run()
    {
        while(true){
            try{
                Message_Music m = (Message_Music) oi.readObject();
                System.out.println("i");
                if(m.t== Message_Music.job.song) {
                    System.out.println(m);
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Select * from MUSIC_SERVER where Name=";
                    q = q + '"';
                    q = q + m.name;
                    q = q + '"';
                    q = q + ';';
                    System.out.println(q);
                    PreparedStatement preSat;
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    if (result.next()) {
                        String args[] = new String[100];
                        args[0] = result.getString("Location1");
                        if (args.length == 0)
                            throw new IllegalArgumentException("expected sound file arg");
                        File soundFile = AudioUtil.getSoundFile(args[0]);
                        System.out.println("server: " + soundFile);

                        try (FileInputStream in = new FileInputStream(soundFile)) {
                            Socket client = this.socket;
                            System.out.println(args[0]);
                            System.out.println("CLIENT CONNECTED: " + args[0]);
                            OutputStream out = client.getOutputStream();


                            byte buffer[] = new byte[1024];
                            int count;
                            while ((count = in.read(buffer)) != -1)
                                out.write(buffer, 0, count);


                        }

                        System.out.println("server: shutdown");
                    }
                }
                else if(m.t== Message_Music.job.playlist_enter){
                    System.out.println(m.name);
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url,"root","root");
                    String q = "Insert into PLAYLIST_NAMES values(?,?,?)";
                    PreparedStatement preSat=null;
                    try{ preSat = connection.prepareStatement(q);
                        preSat.setString(1, m.name);
                        Message_Plalist mi = (Message_Plalist) oi.readObject();
                        preSat.setString(2,mi.name);
                        preSat.setString(3,m.name+'_'+mi.name);
                        System.out.println(q);
                        preSat.execute();
                        q = "Insert into PLAYLIST_SONGS values(?,?)";
                        preSat = connection.prepareStatement(q);
                        for(int k =0;k<mi.k;k++){
                            preSat.setString(1, m.name+'_'+mi.name);
                            preSat.setString(2,mi.s[k]);
                            System.out.println(q);
                            preSat.execute();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else if(m.t== Message_Music.job.sub){

                    System.out.println(m);
                    String url = "jdbc:mysql://localhost:3306/Ampify";
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
                else if(m.t==Message_Music.job.history_send){
                    Thread.sleep(500);
                    System.out.println(m.name);
                    System.out.println("hihihi");
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url,"root","root");
                    String q = "Insert into HISTORY values(?,?,?,?)";
                    PreparedStatement preSat=null;
                    try{ preSat = connection.prepareStatement(q);
                        preSat.setString(1, m.name);
                        Message_History mi = (Message_History) oi.readObject();
                        System.out.println(mi.s[0]);
                    String a = mi.s[0];
                    StringTokenizer st = new StringTokenizer(a,";");
                    preSat.setString(2, st.nextToken());
                    preSat.setString(3,st.nextToken());
                    preSat.setString(4, st.nextToken());
                    System.out.println(q);
                    preSat.execute();}
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
              else if(m.t==Message_Music.job.history_receive){
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url,"root","root");
                    String q = "Select * from HISTORY where UserName=";
                    q = q+'"';
                    q = q+m.name;
                    q = q +'"';
                    q = q+';';
                    System.out.println(q);
                    PreparedStatement preSat;
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    int c=0;
                    while(result.next()){
                        c++;
                    }
                    result = preSat.executeQuery();
                    String l[] = new String[c];
                    int i=0;
                    while(result.next()){
                        l[i] = result.getString("Song")+","+result.getString("Date")+';'+result.getString("Time")+';';
                        i++;
                    }
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    Message_History k = new Message_History(l,i);
                    op.writeObject(k);
                    op.flush();
                    op.close();
                }
              else if(m.t==Message_Music.job.playlist_send){
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url,"root","root");
                    String q = "Select * from PLAYLIST_NAMES where UserName=";
                    q = q+'"';
                    q = q+m.name;
                    q = q +'"';
                    q = q+';';
                    System.out.println(q);
                    PreparedStatement preSat;
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    int c=0;
                    while(result.next()){
                        c++;
                    }
                    result = preSat.executeQuery();
                    String l[] = new String[c];
                    int i=0;
                    while(result.next()){
                        l[i] = result.getString("NamePlayList");
                        i++;
                    }
                    op.writeObject(new Message_Plalist(m.name,l,c));
                    op.flush();
                    String a = m.name;
                    m =(Message_Music) oi.readObject();
                    q = "Select * from PLAYLIST_SONGS where ID=";
                    q = q+'"';
                    q = q+(a+'_'+m.name);
                    q = q +'"';
                    q = q+';';
                    System.out.println(q);
                    preSat = connection.prepareStatement(q);
                    result = preSat.executeQuery();
                    c=0;
                    while(result.next()){
                        c++;
                    }
                    result = preSat.executeQuery();
                    String u[] = new String[c];
                    i=0;
                    while(result.next()){
                        u[i] = result.getString("Song_Name");
                        i++;
                    }
                    op.writeObject(new Message_Plalist(q,u,c));
                    op.flush();
                    op.close();
                }
                else if(m.t==Message_Music.job.Like){
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url,"root","root");
                    String q = "Insert into PLAYLIST_NAMES values(?,?,?)";
                    System.out.println("like: "+q);
                    PreparedStatement preSat=null;
                    try{ preSat = connection.prepareStatement(q);
                        preSat.setString(1, m.name);
                        preSat.setString(2,"Like");
                        preSat.setString(3,m.name+'_'+"Like");
                        System.out.println(q);
                        preSat.execute();}
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else if(m.t==Message_Music.job.Like_Add){
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url,"root","root");
                    String q = "Insert into PLAYLIST_SONGS values(?,?)";
                    System.out.println("like: "+q);
                    PreparedStatement preSat=null;
                    try{ preSat = connection.prepareStatement(q);
                        Message_Music mi = (Message_Music) oi.readObject();
                        preSat.setString(1, m.name+"_Like");
                        preSat.setString(2,mi.name);
                        System.out.println(q);
                        preSat.execute();}
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            catch(Exception e){
                e.printStackTrace();
                return;
            }
        }
    }

}