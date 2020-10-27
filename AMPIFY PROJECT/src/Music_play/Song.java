package Music_play;
import Message.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.*;
import java.io.*;
import java.net.Socket;

public class Song implements Runnable{
    public String c;
    public Song(String a){
        c=a;
    }
    public void run(){
        try {
            final Socket socket = new Socket("127.0.0.1", 5402);
            ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
            sendMessage(socket, c, op);
            System.out.println("Client: reading from 127.0.0.1:5401");
            InputStream l = socket.getInputStream();
            BufferedInputStream in = new BufferedInputStream(l);

            System.out.println(in);
            try {
                play(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    private synchronized void play(final InputStream in) throws Exception {
        AudioInputStream ais = AudioSystem.getAudioInputStream(in);
        try (Clip clip = AudioSystem.getClip()) {
            clip.open(ais);
            clip.start();
            Thread.sleep(100); // given clip.drain a chance to start
            clip.drain();
        }
    }
    public void sendMessage(Socket socket, String name, ObjectOutputStream op)throws IOException{
        op.writeObject(new Message_Music(name,0));
        op.flush();
        return;
    }
}
