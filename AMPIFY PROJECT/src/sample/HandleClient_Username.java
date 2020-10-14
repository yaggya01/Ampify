package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.*;
import java.net.Socket;
public class HandleClient_Username implements Runnable{
    final private Socket socket;
    ObjectInputStream oi;
    ObjectOutputStream op;
    public HandleClient_Username(Socket s,ObjectOutputStream o)
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
            try {
                Message m = (Message) oi.readObject();
                System.out.println(m);
                String url = "jdbc:mysql://localhost:3306/Ampify";
                Connection connection = DriverManager.getConnection(url, "root", "root");
                if (m.k == 0) {
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
                else if(m.k==1)
                {
                    String q = "Select * from USER";
                    PreparedStatement preSat;
                    preSat = connection.prepareStatement(q);
                    ResultSet result = preSat.executeQuery();
                    while(result.next())
                    {
                        System.out.println("User Name: "+result.getString("UserName")+" Password: "+result.getString("Password")+"Email: "+result.getString("EmailID"));
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
            catch(Exception e){
                e.printStackTrace();
                return;
            }


        }
    }
}
