package Server;
import File.*;

import java.nio.file.Files;
import java.nio.file.Paths;
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
            try {
                Message_Music m = (Message_Music) oi.readObject();
                System.out.println("i");
                if (m.t == Message_Music.job.song) {
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
                } else if (m.t == Message_Music.job.playlist_enter) {
                    System.out.println(m.name);
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Insert into PLAYLIST_NAMES values(?,?,?,?)";
                    PreparedStatement preSat = null;
                    try {
                        preSat = connection.prepareStatement(q);
                        preSat.setString(1, m.name);
                        Message_Plalist mi = (Message_Plalist) oi.readObject();
                        preSat.setString(2, mi.name);
                        preSat.setString(3, m.name + '_' + mi.name);
                        preSat.setInt(4,mi.p);
                        System.out.println(q);
                        preSat.execute();
                        q = "Insert into PLAYLIST_SONGS values(?,?)";
                        preSat = connection.prepareStatement(q);
                        for (int k = 0; k < mi.k; k++) {
                            preSat.setString(1, m.name + '_' + mi.name);
                            preSat.setString(2, mi.s[k]);
                            System.out.println(q);
                            preSat.execute();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (m.t == Message_Music.job.sub) {

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
                        args[1] = result.getString("Location2");
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
                } else if (m.t == Message_Music.job.history_send) {
                    Thread.sleep(500);
                    System.out.println(m.name);
                    System.out.println("hihihi");
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Insert into HISTORY values(?,?,?,?,?)";
                    PreparedStatement preSat = null;PreparedStatement preSat1 = null;
                    try {
                        String q1 = "Select * from HISTORY where UserName=";
                        q1 = q1 + '"';
                        q1 = q1 + (m.name);
                        q1 = q1 + '"';
                        q1 = q1 +" and Song = ";
                        Message_History mi = (Message_History) oi.readObject();
                        System.out.println(mi.s[0]);
                        String a = mi.s[0];
                        StringTokenizer st = new StringTokenizer(a, ";");
                        a = st.nextToken();
                        q1 = q1 + '"';
                        q1 = q1 + a;
                        q1 = q1 + '"';
                        q1 = q1 + ';';
                        preSat1=connection.prepareStatement(q1);
                        System.out.println(q1);
                        ResultSet result = preSat1.executeQuery();
                        if(result.next()) {
                            int x=result.getInt("USED_TIMES");
                            while (result.next()){
                                x=result.getInt("USED_TIMES");
                            }
                            preSat = connection.prepareStatement(q);
                            preSat.setString(1, m.name);
                            preSat.setString(2, a);
                            preSat.setString(3, st.nextToken());
                            preSat.setString(4, st.nextToken());
                            preSat.setInt(5, x+1);
                            System.out.println(q);
                            preSat.execute();
                        }
                        else{
                            preSat = connection.prepareStatement(q);
                            preSat.setString(1, m.name);
                            preSat.setString(2, a);
                            preSat.setString(3, st.nextToken());
                            preSat.setString(4, st.nextToken());
                            preSat.setInt(5, 1);
                            System.out.println(q);
                            preSat.execute();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (m.t == Message_Music.job.history_receive) {
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Select * from HISTORY where UserName=";
                    q = q + '"';
                    q = q + m.name;
                    q = q + '"';
                    q = q + ';';
                    System.out.println(q);
                    PreparedStatement preSat;
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    int c = 0;
                    while (result.next()) {
                        c++;
                    }
                    result = preSat.executeQuery();
                    String l[] = new String[c];
                    int i = 0;
                    while (result.next()) {
                        l[i] = result.getString("Song") + "," + result.getString("Date") + ';' + result.getString("Time") + ';';
                        i++;
                    }
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    Message_History k = new Message_History(l, i);
                    op.writeObject(k);
                    op.flush();
                    op.close();
                } else if (m.t == Message_Music.job.playlist_send) {
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Select * from PLAYLIST_NAMES where UserName=";
                    q = q + '"';
                    q = q + m.name;
                    q = q + '"';
                    q = q + ';';
                    System.out.println(q);
                    PreparedStatement preSat;
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    int c = 0;
                    while (result.next()) {
                        c++;
                    }
                    result = preSat.executeQuery();
                    String l[] = new String[c];
                    int i = 0;
                    while (result.next()) {
                        l[i] = result.getString("NamePlayList");
                        i++;
                    }
                    op.writeObject(new Message_Plalist(m.name, l, c,0));
                    op.flush();
                    String a = m.name;
                    m = (Message_Music) oi.readObject();
                    q = "Select * from PLAYLIST_SONGS where ID=";
                    q = q + '"';
                    q = q + (a + '_' + m.name);
                    q = q + '"';
                    q = q + ';';
                    System.out.println(q);
                    preSat = connection.prepareStatement(q);
                    result = preSat.executeQuery();
                    c = 0;
                    while (result.next()) {
                        c++;
                    }
                    result = preSat.executeQuery();
                    String u[] = new String[c];
                    i = 0;
                    while (result.next()) {
                        u[i] = result.getString("Song_Name");
                        i++;
                    }
                    op.writeObject(new Message_Plalist(q, u, c,0));
                    op.flush();
                    op.close();
                } else if (m.t == Message_Music.job.Like) {
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Insert into PLAYLIST_NAMES values(?,?,?,?)";
                    System.out.println("like: " + q);
                    PreparedStatement preSat = null;
                    try {
                        preSat = connection.prepareStatement(q);
                        preSat.setString(1, m.name);
                        preSat.setString(2, "Like");
                        preSat.setString(3, m.name + '_' + "Like");
                        preSat.setInt(4, 1);
                        System.out.println(q);
                        preSat.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (m.t == Message_Music.job.Like_Add) {
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Insert into PLAYLIST_SONGS values(?,?)";
                    System.out.println("like: " + q);
                    PreparedStatement preSat = null;
                    try {
                        preSat = connection.prepareStatement(q);
                        Message_Music mi = (Message_Music) oi.readObject();
                        preSat.setString(1, m.name + "_Like");
                        preSat.setString(2, mi.name);
                        System.out.println(q);
                        preSat.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (m.t == Message_Music.job.delete) {
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Delete from PLAYLIST_SONGS where ID=";
                    q = q + '"';
                    q = q + (m.name);
                    q = q + '"';
                    q = q + ' ';
                    q = q + "AND Song_Name=";
                    Message_Music mi = (Message_Music) oi.readObject();
                    q = q + '"';
                    q = q + (mi.name);
                    q = q + '"';
                    PreparedStatement preSat;
                    preSat = connection.prepareStatement(q);
                    try {
                        preSat.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (m.t == Message_Music.job.Insert) {
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Insert into PLAYLIST_SONGS values(?,?)";
                    System.out.println(q);
                    PreparedStatement preSat = null;
                    try {
                        preSat = connection.prepareStatement(q);
                        preSat.setString(1, m.name);
                        Message_Music mi = (Message_Music) oi.readObject();
                        if(mi.name!=null){
                        preSat.setString(2, mi.name);
                        System.out.println(q);
                        preSat.execute();}
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
                else if(m.t == Message_Music.job.MostPlayed){
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Select * from HISTORY where UserName=";
                    q = q + '"';
                    q = q + m.name;
                    q = q + '"';
                    q = q + ';';
                    System.out.println(q);
                    PreparedStatement preSat;
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    result.next();
                    String a = result.getString("Song");
                    int b=result.getInt("USED_TIMES");
                    while(result.next()){
                        int c = result.getInt("USED_TIMES");;
                        if(c>b){
                            b= c;
                            a = result.getString("Song");
                        }
                    }
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    op.writeObject(new Message_Music(a,0));
                    op.flush();
                }
                else if(m.t == Message_Music.job.Download){
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
                    if(result.next()){

                        File myFile = new File(result.getString("Location1"));
                        while (true) {
                            byte[] mybytearray = new byte[(int) myFile.length()];
                            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
                            bis.read(mybytearray, 0, mybytearray.length);
                            OutputStream os = socket.getOutputStream();
                            os.write(mybytearray, 0, mybytearray.length);
                            os.flush();
                            socket.close();
                        }}


                }
                else if (m.t == Message_Music.job.playlist_send_friend) {
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Select * from PLAYLIST_NAMES where UserName=";
                    q = q + '"';
                    q = q + m.name;
                    q = q + '"';
                    q = q + ';';
                    System.out.println(q);
                    PreparedStatement preSat;
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    int c = 0;
                    while (result.next()) {
                        if(result.getInt("Private")==0){
                            c++;
                        }
                    }
                    result = preSat.executeQuery();
                    String l[] = new String[c];
                    int i = 0;
                    while (result.next()) {
                        if(result.getInt("Private")==0){
                            l[i] = result.getString("NamePlayList");
                            i++;
                        }
                    }
                    op.writeObject(new Message_Plalist(m.name, l, c,0));
                    op.flush();
                    String a = m.name;
                    m = (Message_Music) oi.readObject();
                    q = "Select * from PLAYLIST_SONGS where ID=";
                    q = q + '"';
                    q = q + (a + '_' + m.name);
                    q = q + '"';
                    q = q + ';';
                    System.out.println(q);
                    preSat = connection.prepareStatement(q);
                    result = preSat.executeQuery();
                    c = 0;
                    while (result.next()) {
                        c++;
                    }
                    result = preSat.executeQuery();
                    String u[] = new String[c];
                    i = 0;
                    while (result.next()) {
                        u[i] = result.getString("Song_Name");
                        i++;
                    }
                    op.writeObject(new Message_Plalist(q, u, c,0));
                    op.flush();
                    op.close();
                }
                else if(m.t == Message_Music.job.song_send_friend){
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String a = m.name;
                    Message_Music mi = (Message_Music) oi.readObject();
                    String q = "Select * from PLAYLIST_SONGS where ID=";
                    q = q + '"';
                    q = q + (a + '_' + mi.name);
                    q = q + '"';
                    q = q + ';';
                    System.out.println(q);
                    PreparedStatement preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    int c = 0;
                    while (result.next()) {
                        c++;
                    }
                    result = preSat.executeQuery();
                    String u[] = new String[c];
                    int i = 0;
                    while (result.next()) {
                        u[i] = result.getString("Song_Name");
                        i++;
                    }
                    op.writeObject(new Message_Plalist(q, u, c,0));
                    op.flush();
                    op.close();
                }
            }
            catch(Exception e){
                e.printStackTrace();
                return;
            }
        }
    }

}