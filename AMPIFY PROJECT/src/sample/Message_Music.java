package sample;

import java.io.Serializable;
public class Message_Music implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String name;
    public int i;
    public Message_Music(String n,int j){
        name = n;
        i=j;
    }
    public String toString(){
        return String.format("Name: %s",name);
    }
}
