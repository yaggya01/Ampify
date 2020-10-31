package Message;

import java.io.Serializable;
public class Message_Server implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int m ;
    public String n;
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
        else if(m==2)
        {
            n = "Password Incorrect";
        }
        else if(m==3){
            n= "User Found Request Send";
        }

    }
    public String toString(){
        return String.format("BOOL");
    }
}
