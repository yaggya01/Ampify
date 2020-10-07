package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.*;
import java.net.Socket;
public class HandleClient_Username implements Runnable{
    private Socket socket;
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
            try{
                Message m = (Message) oi.readObject();
                System.out.println(m);
                String url = "jdbc:mysql://localhost:3306/school";
                Connection connection = DriverManager.getConnection(url,"root","root");
                String q = "Select * from USER where UserName=";
                q = q+'"';
                q = q+m.name;
                q = q +'"';
                q = q+';';
                System.out.println(q);
                PreparedStatement preSat;
                preSat = connection.prepareStatement(q);
                ResultSet result = preSat.executeQuery();
                Message_Server k ;
                if(result.next())
                {
                    String g = result.getString("Password");
                    System.out.println(g);
                    if(m.password == g)
                    {
                        System.out.println("de");
                        k = new Message_Server(0);
                    }
                    else
                    {System.out.println("df");
                        k = new Message_Server(2);
                    }
                }
                else
                {
                    k = new Message_Server(1);
                }
                op.writeObject(k);
                op.flush();

            }
            catch(Exception e){
                e.printStackTrace();
                return;
            }
        }
    }
}
