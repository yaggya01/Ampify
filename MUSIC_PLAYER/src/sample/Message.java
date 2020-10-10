package sample;

import java.io.Serializable;
public class Message implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String name;

    public Message(String n){
        name = n;
    }
    public String toString(){
        return String.format("Name: %s",name);
    }
}
