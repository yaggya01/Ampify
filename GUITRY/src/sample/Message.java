package sample;

import java.io.Serializable;
public class Message implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String name;
    public String password;

    public Message(String n,String p){
        name = n;
        password = p;
    }
    public String toString(){
        return String.format("UserName: %s\nPassword: %s",name,password);
    }
}
