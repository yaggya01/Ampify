package sample;

import java.io.Serializable;
public class Message_Server implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int m ;
    String n;
    public Message_Server(int b){
        m=b;
        if(m == 0)
        {
            n = "LOGED IN";
        }
        else if(m==1)
        {
            n = "USERNOT FOUND";
        }
        else
        {
            n = "Password Incorrect";
        }
    }
    public String toString(){
        return String.format("BOOL");
    }
}
