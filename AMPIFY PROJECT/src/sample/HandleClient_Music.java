package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

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
                if(m.i==0) {
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


                            byte buffer[] = new byte[2048];
                            int count;
                            while ((count = in.read(buffer)) != -1)
                                out.write(buffer, 0, count);


                        }

                        System.out.println("server: shutdown");
                    }
                }
                else if(m.i==1){
                    System.out.println(m.name);
                    StringTokenizer st = new StringTokenizer(m.name,";");
                    int k =st.countTokens();
                    String u = st.nextToken();
                    String g = st.nextToken();
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String f="";
                    for(int i=0;i<k-2;i++){
                        f = f+st.nextToken()+";";
                    }
                    String query1 = "Insert into PLAYLIST values (?,?,?)";
                    PreparedStatement preSat = connection.prepareStatement(query1);
                    preSat.setString(1, u);
                    preSat.setString(2, g);
                    preSat.setString(3, f);
                    System.out.println(query1);
                    preSat.execute();

                }
                else if(m.i==2){

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
            }

            catch(Exception e){
                e.printStackTrace();
                return;
            }
        }
    }

}