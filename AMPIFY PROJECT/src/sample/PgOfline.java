package sample;


import javax.sound.sampled.*;

import Music_play.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import File.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class PgOfline {
    public Button playBT;
    public Button musicBT;
    public Button subBT;
    public Label subLB;
    public static String musicf;
    public static String subf;
    public Button backBT;
    public TextArea songsTA;
    public TextField songTF;
    public static String song;
    //Listener function of Button enter mp3 song
    public void lbtm(ActionEvent actionEvent){
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(null);
        if(f!=null){
            musicf = f.getAbsolutePath();
        }
    }
    //Listener function of Button enter subtitles file
    public void lbts(ActionEvent actionEvent){
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(null);
        if(f!=null){
            subf = f.getAbsolutePath();
        }
    }
    //Listener function of Button play
    public void lbtp(ActionEvent actionEvent){
        System.out.println("starting");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String a = musicf;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String b = subf;
                        File subfile = FileUtil.getFile(b);
                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(subfile)));
                            String line;
                            while ((line = in.readLine()) != null)
                            {
                                String a[] = new String [4];
                                for(int i=0;i<3;i++){
                                    a[i] = line;
                                    line = in.readLine();
                                }
                                a[3] = line;
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        subLB.setText(a[2]);
                                    }
                                });
                                int t1 = Integer.parseInt(a[1].substring(6,8))+Integer.parseInt(a[1].substring(3,5))*60;
                                int t2 = Integer.parseInt(a[1].substring(23,25))+Integer.parseInt(a[1].substring(20,22))*60;
                                Thread.sleep((t2-t1)*1000+300);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Mp3Player m = new Mp3Player(a);
                m.play();

            }
            private synchronized void play(final BufferedInputStream in) throws Exception {
                AudioInputStream ais = AudioSystem.getAudioInputStream(in);
                try (Clip clip = AudioSystem.getClip()) {
                    clip.open(ais);
                    clip.start();
                    Thread.sleep(100); // given clip.drain a chance to start
                    clip.drain();
                }
            }
        }).start();
    }
    //Listener function of Button back
    public void lbtb(ActionEvent actionEvent){
        System.out.println("BACK");
        Parent root=null;
        Stage stage = (Stage) backBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./pg1.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,600, 400));
    }
    //Listener function of Button start to print name of files
    public void lbtstart(ActionEvent actionEvent) throws Exception {
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
                    songsTA.appendText(arr[index].getName()+"\n");

                    // for sub-directories
                else if(arr[index].isDirectory())
                {
                    songsTA.appendText("[" + arr[index].getName() + "]\n");

                    // recursion for sub-directories
                    RecursivePrint(arr[index].listFiles(), 0, level + 1);
                }

                // recursion for main directory
                RecursivePrint(arr,++index, level);
            }

        }).start();
    }
    //Listener function of Button play mp3
    public void lbtplay(ActionEvent actionEvent){
        song = "C:\\Users\\dell\\AMPIFY PROJECT\\Songs\\"+songTF.getText();
        System.out.println(song);
        Parent root=null;
        Stage stage = (Stage) new Stage();
        try{
            root = FXMLLoader.load(getClass().getResource("./sound4.fxml"));
        } catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,400, 200));
        stage.show();
    }
    public String getName(){
        System.out.println(this.song);
        return this.song;
    }
}
