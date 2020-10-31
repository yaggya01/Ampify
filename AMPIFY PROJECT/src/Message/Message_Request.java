package Message;

import java.io.Serializable;

public class Message_Request implements Serializable {
    /**
     *
     */
    public String[] s;
    public int k;
    public Message_Request( String[] a,int i){
        k =i ;
        s = new String[i];
        for(int j=0;j<i;j++){
            s[j]=a[j];
        }
    }

}