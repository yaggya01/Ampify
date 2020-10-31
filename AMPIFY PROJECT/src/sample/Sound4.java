package sample;

import Message.Message_Music;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.io.*;
import java.net.Socket;

public class Sound4 extends PgOfline {
    public Button pauseBT;
    public Button stopBT;
    public Button restartBT;
    public Button resumeBT;
    public Button startBT;
    public Button jumpBT;
    public Label subLB;
    int currentFrame;
    public Slider volumeS;
    public TextField jumpTF;
    public static BufferedInputStream in;
    public static BufferedReader in1;
    Clip clip;
    public Label timeLB;
    public Slider timeBT;
    // current status of clip
    String status;
    AudioInputStream audioInputStream;
    static String filePath;
    //Listener function of Button pause
    public void lbtp(ActionEvent actionEvent){
        System.out.println("Pause");
        new Thread(() -> {
            if (status.equals("paused"))
            {
                System.out.println("audio is already paused");
                return;
            }
            currentFrame = clip.getFramePosition();
            clip.stop();
            status = "paused";
            System.out.println(currentFrame);
        }).start();

    }
    //Listener function of Button stop
    public void lbts(ActionEvent actionEvent){
        new Thread(() -> {
            currentFrame = 0;
            clip.stop();
            clip.close();
            status="stopped";
            Platform.runLater(() -> {
                System.out.println("Stop");
                Stage stage = (Stage) stopBT.getScene().getWindow();
                // do what you have to do
                stage.close();
            });
        }).start();

    }
    //Listener function of Button resume
    public void lbtr(ActionEvent actionEvent) {
        System.out.println("Resume");
        new Thread(() -> {
            if (status.equals("play"))
            {
                System.out.println("Audio is already being played");
                return;
            }
            clip.close();
            try {
                resetAudioStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(currentFrame);
            clip.setFramePosition(currentFrame);
            play();
        }).start();

    }
    //Listener function of Button restart
    public void lbtre(ActionEvent actionEvent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        System.out.println("Restart");
        new Thread(() -> {
            clip.stop();
            clip.close();
            try {
                resetAudioStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
            currentFrame = 0;
            clip.setFramePosition(0);
            play();
        }).start();

    }
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException,
            LineUnavailableException
    {
        audioInputStream = AudioSystem.getAudioInputStream(getIStream());
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public File getIStream(){

        String c = getName();
        System.out.println(c);
        System.out.println(c);
        File sf = new File(c).getAbsoluteFile();
        return sf;
    }



    public void sendMessage(int i,Socket socket, String name, ObjectOutputStream op)throws IOException{
        op.writeObject(new Message_Music(name,i));
        op.flush();
        return;
    }
    public void lbtstart(ActionEvent actionEvent)throws Exception{
        audioInputStream = AudioSystem.getAudioInputStream(this.getIStream());
        // create clip reference
        clip = AudioSystem.getClip();

        // open audioInputStream to the clip
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        timeBT.setMax(clip.getFrameLength());
        new Thread(() -> {
            while(currentFrame!=clip.getFrameLength()){
                timeBT.setValue(currentFrame);
                int b = currentFrame/100000;
                int x = b/60;
                int y = b%60;
                String abd = x+" : "+y;

                Platform.runLater(() -> timeLB.setText(abd));
                currentFrame = clip.getFramePosition();
                while(status.equals("paused")){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeS.setMax(1);
        new Thread(() -> {
            while(true){
                double gain = volumeS.getValue();
                float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            }
        }).start();
        double gain = 0.5;
        float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
        this.play();
        jumpTF.setPromptText(String.valueOf(clip.getFrameLength()));
    }
    public void play()
    {
        //start the clip
        clip.start();

        status = "play";
    }
    public void lbtj(ActionEvent actionEvent) throws Exception{
        int c = Integer.parseInt(jumpTF.getText());
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(clip.getFrameLength());
                if (c > 0 && c < clip.getFrameLength())
                {
                    clip.stop();
                    clip.close();
                    try {
                        resetAudioStream();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    currentFrame = c;
                    clip.setFramePosition(c);
                    play();
                }
            }
        }).start();
    }
}


