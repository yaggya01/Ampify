package Message;

import java.io.Serializable;

public class Message_History implements Serializable {
    /**
     *
     */
    public String[] s;
    public int k;
    public Message_History( String[] a,int i){
        k =i ;
        s = new String[i];
        for(int j=0;j<i;j++){
            s[j]=a[j];
        }
    }

}