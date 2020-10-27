package sample;

import Message.Message_Music;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.io.*;
import java.net.Socket;

public class Sound2 extends PlayPL {
    public Button pauseBT;
    public Button stopBT;
    public Button restartBT;
    public Button resumeBT;
    public Button startBT;
    public Label subLB;
    int currentFrame;
    public Slider volumeS;
    public static BufferedInputStream in;
    public static BufferedReader in1;
    Clip clip;
    public Label timeLB;
    public Slider timeBT;
    // current status of clip
    String status;
    public static AudioInputStream audioInputStream;
    static String filePath;

    public Sound2() throws IOException {
    }

    public void lbtpl(ActionEvent actionEvent){
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
    public void lbtsp(ActionEvent actionEvent){
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
    public BufferedInputStream getIStream(){

        String c = getName();
        final Socket socket;
        try {
            socket = new Socket("127.0.0.1", 5402);
            ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
            sendMessage(0,socket, c, op);
            System.out.println("Client: reading from 127.0.0.1:5401");
            InputStream l = socket.getInputStream();
            in = new BufferedInputStream(l);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }
    public BufferedReader getBStream(){
        String c = getName();
        final Socket socket;
        try {
            socket = new Socket("127.0.0.1", 5402);
            ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
            sendMessage(2,socket, c, op);
            System.out.println("Client: reading from 127.0.0.1:5401");
            InputStream l = socket.getInputStream();
            try {
                in1 = new BufferedReader(new InputStreamReader(l, "UTF-8"));
                System.out.println(in1);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in1;
    }
    public void printsub(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    BufferedReader in1 = getBStream();
                    System.out.println(in1);
                    String line;
                    while ((line = in1.readLine()) != null) {
                        String a[] = new String[4];
                        for (int i = 0; i < 3; i++) {
                            a[i] = line;
                            line = in1.readLine();
                        }
                        a[3] = line;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                subLB.setText(a[2]);
                            }
                        });
                        int t1 = Integer.parseInt(a[1].substring(6, 8)) + Integer.parseInt(a[1].substring(3, 5)) * 60;
                        int t2 = Integer.parseInt(a[1].substring(23, 25)) + Integer.parseInt(a[1].substring(20, 22)) * 60;
                        int t3 = Integer.parseInt(a[1].substring(26, 29)) - Integer.parseInt(a[1].substring(9, 12));
                        Thread.sleep((t2 - t1) * 1000 + t3);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public void sendMessage(int i,Socket socket, String name, ObjectOutputStream op)throws IOException{
        op.writeObject(new Message_Music(name,i));
        op.flush();
        return;
    }
    public void lbtstart(ActionEvent actionEvent) throws Exception{
        BufferedInputStream l = this.getIStream();
        System.out.println(l);
        filePath = "C:\\Users\\dell\\Desktop\\I Don't Care.wav";
        System.out.println("3");
        audioInputStream =
                AudioSystem.getAudioInputStream(l);
        System.out.println("1");
        // create clip reference

        clip = AudioSystem.getClip();

        // open audioInputStream to the clip
        clip.open(audioInputStream);
        this.printsub();
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
    }
    public void play()
    {
        //start the clip
        clip.start();

        status = "play";
    }
}

