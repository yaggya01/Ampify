package sample;

import java.io.Serializable;
public class Message implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String name;
    public String password;
    public String email;
    public int k;
    public Message(String n,String p,String e, int i){
        name = n;
        password = p;
        email=e;
        k=i;
    }
    public String toString(){
        return String.format("UserName: %s\nPassword: %s",name,password);
    }
}
