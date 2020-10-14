package sample;

import java.io.Serializable;
public class Message_Music implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String name;

    public Message_Music(String n){
        name = n;
    }
    public String toString(){
        return String.format("Name: %s",name);
    }
}
