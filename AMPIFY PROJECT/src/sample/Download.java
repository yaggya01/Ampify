package sample;

import Message.Message_Music;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Download {
    public Button backBT;
    public Button startBT;
    public Button playBT;
    public TextArea musicTA;
    public Button downloadBT;
    public TextField musicTF;
    public TextField playTF;
    public TextArea songsTA;
    public static String song;
    public void lbts(javafx.event.ActionEvent actionEvent) throws InterruptedException, IOException {
        System.out.println("starting");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://localhost:3306/Ampify";
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String maindirpath = "C:\\Users\\dell\\AMPIFY PROJECT\\Ofline_Song";

                // File object
                File maindir = new File(maindirpath);

                if(maindir.exists() && maindir.isDirectory())
                {
                    // array for files and sub-directories
                    // of directory pointed by maindir
                    File arr[] = maindir.listFiles();
                    // Calling recursive method
                    RecursivePrint(arr,0,0);
                }
            }
            void RecursivePrint(File[] arr,int index,int level)
            {
                // terminate condition
                if(index == arr.length)
                    return;

                // tabs for internal levels
                for (int i = 0; i < level; i++)
                    System.out.print("\t");

                // for files
                if(arr[index].exists()) {
                    if(arr[index].isFile())
                        songsTA.appendText(arr[index].getName() + "\n");

                        // for sub-directories
                    else if (arr[index].isDirectory()) {
                        songsTA.appendText("[" + arr[index].getName() + "]\n");

                        // recursion for sub-directories
                        RecursivePrint(arr[index].listFiles(), 0, level + 1);
                    }
                }
                else{
                    return;
                }

                // recursion for main directory
                RecursivePrint(arr,++index, level);
            }

        }).start();
    }

    public void lbtb(javafx.event.ActionEvent actionEvent) {
        System.out.println("All Songs");
        Parent root=null;
        Stage stage = (Stage) backBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./pg1.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,600, 700));
    }
    public void lbtp(javafx.event.ActionEvent actionEvent) {
        String c = musicTF.getText();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("127.0.0.1", 5402);
                    ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
                    sendMessage(11, socket, c, op);
                    System.out.println("Client: reading from 127.0.0.1:5401");
                    String a = "C:\\Users\\dell\\AMPIFY PROJECT\\Ofline_Song\\";
                    a = a + c + ".wav";
                    System.out.println(a);
                    byte[] mybytearray = new byte[1024];
                    InputStream is = socket.getInputStream();
                    FileOutputStream fos = new FileOutputStream(a);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int bytesRead=0;
                    while((bytesRead = is.read(mybytearray, 0, mybytearray.length))!=-1){
                    bos.write(mybytearray, 0, bytesRead);}
                    bos.close();
                    socket.close();


                    //

                    /*File file = new File(a);
                    FileWriter fr = new FileWriter(file, true);
                    BufferedWriter br = new BufferedWriter(fr);
                    br.write("\n");
                    br.write("9405971255");

                    br.close();
                    fr.close();*/

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
            public void sendMessage(int i,Socket socket, String name, ObjectOutputStream op)throws IOException{
                op.writeObject(new Message_Music(name,i));
                op.flush();
            }
        }).start();


    }
    public void lbtplay(javafx.event.ActionEvent actionEvent) throws IOException {
        song = "C:\\Users\\dell\\AMPIFY PROJECT\\Ofline_Song\\";
        song = song+playTF.getText();
        song = song+".wav";

        System.out.println(song);


        /*File file = new File(song);
        FileWriter fr = new FileWriter(file, true);
        BufferedWriter br = new BufferedWriter(fr);

        // BufferedReader object for input.txt
        BufferedReader br1 = new BufferedReader(new FileReader(song));

        String line1 = br1.readLine();

        // loop for each line of input.txt
        while(line1 != null)
        {
            boolean flag = false;

            // BufferedReader object for delete.txt
            String a = "9405971255";

            // loop for each line of delete.txt
            if(line1.equals(a)){
                flag = true;
            }

            // if flag = false
            // write line of input.txt to output.txt
            if(!flag) {
                br.write(line1+"\n");
            }

            line1 = br1.readLine();

        }

        br.flush();

        // closing resources
        br1.close();
        br.close();*/
        System.out.println("File operation performed successfully");


        Parent root=null;
        Stage stage = (Stage) new Stage();
        try{
            root = FXMLLoader.load(getClass().getResource("./sound3.fxml"));
        } catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,400, 200));
        stage.show();
    }
    public String getName(){
        return song;
    }
}
