package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

import Message.*;

public class HandleClient_Username implements Runnable{
    final private Socket socket;
    ObjectInputStream oi;
    public HandleClient_Username(Socket s)
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
            try {
                Message m = (Message) oi.readObject();
                System.out.println(m);
                if (m.t == Message.job.login) {
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    if (m.k == 0) {
                        ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                        String q = "Select * from USER where UserName=";
                        q = q + '"';
                        q = q + m.name;
                        q = q + '"';
                        q = q + ';';
                        System.out.println(q);
                        PreparedStatement preSat;
                        preSat = connection.prepareStatement(q);
                        ResultSet result = preSat.executeQuery();
                        Message_Server k;
                        if (result.next()) {
                            String g = result.getString("Password");
                            if (m.password.equals(g)) {
                                k = new Message_Server(0);
                            } else {
                                k = new Message_Server(2);
                            }
                        } else {
                            k = new Message_Server(1);
                        }
                        op.writeObject(k);
                        op.flush();
                    }
                    else if (m.k == 1) {
                        String q = "Select * from USER";
                        PreparedStatement preSat;
                        preSat = connection.prepareStatement(q);
                        ResultSet result = preSat.executeQuery();
                        while (result.next()) {
                            System.out.println("User Name: " + result.getString("UserName") + " Password: " + result.getString("Password") + "Email: " + result.getString("EmailID"));
                        }
                        String query1 = "Insert into USER values (?,?,?)";
                        preSat = connection.prepareStatement(query1);
                        preSat.setString(1, m.name);
                        preSat.setString(2, m.password);
                        preSat.setString(3, m.email);
                        System.out.println(query1);
                        preSat.execute();
                    }
                }
                else if(m.t==Message.job.friendrequest){
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Select * from USER where UserName=";
                    q = q + '"';
                    q = q + m.name;
                    q = q + '"';
                    q = q + ';';
                    System.out.println(q);
                    PreparedStatement preSat;
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    Message_Server k;
                    if (result.next()) {
                        k = new Message_Server(3);
                    } else {
                        k = new Message_Server(1);
                    }
                    op.writeObject(k);
                    op.flush();
                    q = "INSERT INTO REQUEST values(?,?)";
                    preSat = connection.prepareStatement(q);
                    preSat.setString(1, m.name);
                    preSat.setString(2, m.password);
                    System.out.println(q);
                    preSat.execute();
                }
                else if(m.t==Message.job.friendrequestlist){

                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Select * from REQUEST where UserName=";
                    q = q + '"';
                    q = q + m.name;
                    q = q + '"';
                    q = q + ';';
                    System.out.println(q);
                    PreparedStatement preSat;
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();

                    int c=0,i=0;
                   while(result.next()) {
                        c++;
                   }
                   result = preSat.executeQuery();
                   int l =c;
                   String a[]= new String[l];
                   while(result.next()) {
                        a[i]=result.getString("Friend");
                        i++;
                   }
                   Message_Request mi = new Message_Request(a,l);
                   ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                   op.writeObject(mi);
                }
                else if(m.t==Message.job.friendList){

                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Select * from FRIENDS where UserName=";
                    q = q + '"';
                    q = q + m.name;
                    q = q + '"';
                    q = q + ';';
                    System.out.println(q);
                    PreparedStatement preSat;
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    int c=0,i=0;
                    while(result.next()) {
                        c++;
                    }
                    result = preSat.executeQuery();
                    int l =c;
                    String a[]= new String[l];
                    while(result.next()) {
                        a[i]=result.getString("Friend");
                        i++;
                    }
                    Message_Request mi = new Message_Request(a,l);
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    op.writeObject(mi);
                }
                else if(m.t==Message.job.accept){
                    PreparedStatement preSat;
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Insert Into Friends values(?,?)";
                    preSat = connection.prepareStatement(q);
                    preSat.setString(1, m.name);
                    preSat.setString(2, m.password);
                    System.out.println(q);
                    preSat.execute();
                    q = "Insert Into Friends values(?,?)";
                    preSat = connection.prepareStatement(q);
                    preSat.setString(1, m.password);
                    preSat.setString(2, m.name);
                    System.out.println(q);
                    preSat.execute();
                    q = "DELETE FROM REQUEST WHERE UserName = ";
                    q = q+'"'+m.name+'"'+';';
                    preSat = connection.prepareStatement(q);
                    preSat.execute();
                }
                else if(m.t==Message.job.groups){
                    PreparedStatement preSat;
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Select * from Group_Name";
                    System.out.println(q);
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    int c=0,i=0;
                    while(result.next()) {
                        String b = result.getString("USERS");
                        System.out.println(b);
                        StringTokenizer st = new StringTokenizer(b,";");
                        int j = st.countTokens();
                        for(int k =0;k<j;k++){
                            if(st.nextToken().equals(m.name)){
                                c++;
                                break;
                            }
                        }
                    }
                    System.out.println(c);
                    result = preSat.executeQuery();
                    int l =c;
                    String a[]= new String[l];
                    while(result.next()) {
                        String b = result.getString("USERS");
                        StringTokenizer st = new StringTokenizer(b,";");
                        int j = st.countTokens();
                        for(int k =0;k<j;k++){
                            if(st.nextToken().equals(m.name)){
                                a[i]=result.getString("Gorup_Name");
                                System.out.println(a[i]);
                                i++;
                                break;
                            }
                        }
                    }
                    Message_Request mi = new Message_Request(a,l);
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    op.writeObject(mi);
                }
                else if(m.t==Message.job.group_playlist){
                    PreparedStatement preSat;
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Select * from Group_Name";
                    System.out.println(q);
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    int c=0,i=0;
                    while(result.next()) {
                        String b = result.getString("USERS");
                        System.out.println(b);
                        StringTokenizer st = new StringTokenizer(b,";");
                        int j = st.countTokens();
                        System.out.println(m.name+" "+m.password);
                        for(int k =0;k<j;k++){
                            if(st.nextToken().equals(m.name)){
                               if(result.getString("Gorup_Name").equals(m.password)){
                                c++;
                                break;}
                            }
                        }
                    }
                    System.out.println(c);
                    result = preSat.executeQuery();
                    int l =c;
                    String a[]= new String[l];
                    while(result.next()) {
                        String b = result.getString("USERS");
                        StringTokenizer st = new StringTokenizer(b,";");
                        int j = st.countTokens();
                        for(int k =0;k<j;k++){
                            if(st.nextToken().equals(m.name)){
                                if(result.getString("Gorup_Name").equals(m.password)){
                                    String x =result.getString("ID");
                                    q = "Select * from Group_Playlist_Name where Group_Name_ID=";
                                    q=q+'"'+x+'"'+';';
                                    preSat = connection.prepareStatement(q);
                                    ResultSet r = preSat.executeQuery();
                                    if(r.next()){
                                    a[i]=r.getString("ID");
                                    System.out.println(a[i]);
                                    i++;
                                    break;}
                                }
                            }
                        }
                    }
                    Message_Request mi = new Message_Request(a,l);
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    op.writeObject(mi);
                }
                else if(m.t==Message.job.group_playlist_song){
                    PreparedStatement preSat;
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Select * from Playlist_Songs where ID=";
                    q=q+'"'+m.name+'"'+';';
                    System.out.println(q);
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    int c=0,i=0;
                    while(result.next()) {
                        c++;
                    }
                    System.out.println(c);
                    result = preSat.executeQuery();
                    int l =c;
                    String a[]= new String[l];
                    while(result.next()) {
                        a[i] = result.getString("Song_Name");
                        i++;
                    }
                    Message_Request mi = new Message_Request(a,l);
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    op.writeObject(mi);
                }
                else  if(m.t==Message.job.new_group){
                    PreparedStatement preSat;
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Insert Into Group_Name values(?,?,?)";
                    preSat = connection.prepareStatement(q);
                    preSat.setString(1, m.name);
                    preSat.setString(2, m.password);
                    preSat.setString(3, m.name+'_'+m.password);
                    System.out.println(q);
                    preSat.execute();
                }
                else  if(m.t==Message.job.new_playlist){
                    PreparedStatement preSat;
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Select * from Group_Name where GORUP_NAME=";
                    q = q+'"'+m.name+'"'+';';
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    System.out.println(q);
                    String v="";
                    int f=0;
                    while(result.next()) {
                        String b = result.getString("USERS");
                        System.out.println(b);
                        StringTokenizer st = new StringTokenizer(b,";");
                        int j = st.countTokens();
                        System.out.println(m.name+" "+m.password);
                        for(int k =0;k<j;k++){
                            if(st.nextToken().equals(m.password)){
                                    v = result.getString("ID");
                                    f=1;
                                    break;
                            }
                        }
                        if(f==1){
                            break;
                        }
                    }

                    System.out.println(v);
                    Message_Plalist mi = (Message_Plalist) oi.readObject();
                    q = "Insert into group_Playlist_name values(?,?,?)";
                    preSat = connection.prepareStatement(q);
                    preSat.setString(1, v);
                    preSat.setString(2, m.email);
                    preSat.setString(3, m.email+'_'+v);
                    System.out.println(q);
                    preSat.execute();
                    String id = m.email+'_'+v;
                    for(int i=0;i<mi.k;i++) {
                        q = "Insert into Playlist_Songs values(?,?)";
                        preSat = connection.prepareStatement(q);
                        preSat.setString(1, id);
                        preSat.setString(2, mi.s[i]);
                        preSat.execute();
                    }

                }
                else if(m.t==Message.job.select){
                    PreparedStatement preSat;
                    String url = "jdbc:mysql://localhost:3306/Ampify";
                    Connection connection = DriverManager.getConnection(url, "root", "root");
                    String q = "Update User set Language=";
                    q=q+'"'+m.password+'"'+"Where UserName="+'"'+m.name+'"'+';';
                    preSat = connection.prepareStatement(q);
                    preSat.execute();
                    String a="",b;
                    int k=0;
                    while(m.email.charAt(k)!='_'){
                        a =a+m.email.charAt(k);
                        k++;
                    }
                    int l = m.email.length();
                    b = m.email.substring(k,l);

                    q = "Update User set Artist=";
                    q=q+'"'+a+'"'+"Where UserName="+'"'+m.name+'"'+';';
                    preSat = connection.prepareStatement(q);
                    preSat.execute();
                    q = "Update User set Gerner=";
                    q=q+'"'+b+'"'+"Where UserName="+'"'+m.name+'"'+';';
                    preSat = connection.prepareStatement(q);
                    preSat.execute();
                }
            }
            catch(Exception e){
                    e.printStackTrace();
                    return;
                }


        }
    }
}
