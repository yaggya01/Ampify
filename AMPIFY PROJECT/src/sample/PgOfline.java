package sample;


import javax.sound.sampled.*;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;

public class PgOfline {
    public Button playBT;
    public TextField musicTF;
    public TextField subTF;
    public Label subLB;
    public void lbtp(ActionEvent actionEvent){
        System.out.println("starting");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String a = musicTF.getText();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String b = subTF.getText();
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
                File soundFile = AudioUtil.getSoundFile(a);
                try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(soundFile))) {
                    play(in);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
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
}
