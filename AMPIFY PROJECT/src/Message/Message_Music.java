package Message;

import java.io.Serializable;

public class Message_Music implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String name;
    public enum job{
        song,sub,playlist_enter,playlist_send,history_send, history_receive, Like, Like_Add,delete, Insert, MostPlayed, Download
    }
    public job t;
    public Message_Music(String n,int j){
        name = n;
        if(j==0){
            t = job.song;
        }
        else if(j==2){
            t = job.sub;
        }
        else if(j==1){
            t = job.playlist_enter;
        }
        else  if(j==3){
            t = job.history_send;
        }
        else if(j==4){
            t = job.history_receive;
        }
        else if(j==5){
            t=job.playlist_send;
        }
        else if(j==6){
            t =job.Like;
        }
        else if (j==7){
            t = job.Like_Add;
        }
        else if(j==8){
            t = job.delete;
        }
        else if(j==9){
            t = job.Insert;
        }
        else if(j==10){
            t = job.MostPlayed;
        }
        else if(j==11){
            t = job.Download;
        }
    }
    public String toString(){
        return String.format("Name: %s",name);
    }
}
