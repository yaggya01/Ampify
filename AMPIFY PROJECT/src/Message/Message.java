package Message;

import java.io.Serializable;
public class Message implements Serializable{
    /**
     *
     */
    public enum job{
        login,friendrequest,friendrequestlist,friendList, accept, groups, group_playlist, group_playlist_song, new_group, new_playlist
    }
    private static final long serialVersionUID = 1L;
    public String name;
    public String password;
    public String email;
    public int k;
    public job t;
    public Message(String n,String p,String e, int i,int j){
        name = n;
        password = p;
        email=e;
        k=i;
        if(j==0){
            t = job.login;
        }
        else if(j==1){
            t = job.friendrequest;
        }
        else if(j==2){
            t = job.friendrequestlist;
        }
        else if(j==3){
            t = job.friendList;
        }
        else if(j==4){
            t = job.accept;
        }
        else if(j==5){
            t = job.groups;
        }
        else if(j==6){
            t = job.group_playlist;
        }
        else if(j==7){
            t = job.group_playlist_song;
        }
        else if(j==8){
            t = job.new_group;
        }
        else if(j==9){
            t = job.new_playlist;
        }
    }
    public String toString(){
        return String.format("UserName: %s\nPassword: %s",name,password);
    }
}
