package sample;

import Message.Message_Music;
import Server.HandleClient_Music;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import File.*;
public class Songshare {
    public Button sendBT;
    public TextField songTF;
    public Button playBT;
    public Button recieveBT;
    public TextArea songTA;
    public static Socket socket[] = new Socket[10];
    public static int i = 0;
    public void lbts(ActionEvent actionEvent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String maindirpath = "C:\\Users\\dell\\AMPIFY PROJECT\\Songs";

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
                if(arr[index].isFile())
                    songTA.appendText(arr[index].getName()+"\n");

                    // for sub-directories
                else if(arr[index].isDirectory())
                {
                    songTA.appendText("[" + arr[index].getName() + "]\n");

                    // recursion for sub-directories
                    RecursivePrint(arr[index].listFiles(), 0, level + 1);
                }

                // recursion for main directory
                RecursivePrint(arr,++index, level);
            }

        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocket serversocket;
                Socket s;
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Server Started: ");
                try{
                    serversocket = new ServerSocket(5403);//port number
                }
                catch(IOException e){
                    e.printStackTrace();
                    return;
                }
                while(true)
                {
                    try{
                        s = serversocket.accept();
                        System.out.println("Connected to Client: ");
                        socket[i]=s;
                        i++;
                    }
                    catch(IOException e){
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }).start();
    }

    public void lbtp(ActionEvent actionEvent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getIStream());
                // create clip reference
                Clip clip = null;

                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // open audioInputStream to the clip

            }
            public File getIStream(){

                String c =  "C:\\Users\\dell\\AMPIFY PROJECT\\Songs\\"+songTF.getText();
                System.out.println(c);
                System.out.println(c);
                File sf = new File(c).getAbsoluteFile();
                return sf;
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String c =  "C:\\Users\\dell\\AMPIFY PROJECT\\Songs\\"+songTF.getText();
                File soundFile = AudioUtil.getSoundFile(c);
                System.out.println("server: " + soundFile);

                for(int j=0;j<i;j++) {
                    try (FileInputStream in = new FileInputStream(soundFile)) {
                        Socket client = socket[j];
                        System.out.println(c);
                        System.out.println("CLIENT CONNECTED: " + c);
                        OutputStream out = client.getOutputStream();


                        byte buffer[] = new byte[1024];
                        int count;
                        while ((count = in.read(buffer)) != -1)
                            out.write(buffer, 0, count);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void lbtr(ActionEvent actionEvent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket s = new Socket("localhost",5403);
                    InputStream i = s.getInputStream();
                    BufferedInputStream bi = new BufferedInputStream(i);
                    System.out.println(i);
                    while(s.isConnected()){
                        if(i!=null){
                            System.out.println("TRUE");
                            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bi);
                            // create clip reference
                            Clip clip = AudioSystem.getClip();
                            clip.open(audioInputStream);
                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
