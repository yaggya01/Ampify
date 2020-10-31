package sample;

import Message.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Message.*;
import java.io.*;
import java.net.Socket;

public class Groupcreate extends Group {
    public Button backBT;
    public Button friendBT;
    public TextArea friendTA;
    public Button groupBT;
    public TextField groupTF;
    public Button nameBT;
    public TextField friendTF;
    public static String groupname;
    public static String users;
    public void lbtb(ActionEvent actionEvent){
        System.out.println("All Songs");
        Parent root=null;
        Stage stage = (Stage) backBT.getScene().getWindow();
        try{
            root = FXMLLoader.load(getClass().getResource("./group.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,300, 275));
    }
    public void lbtf(ActionEvent actionEvent) throws Exception {
        Socket socket1 = new Socket("localhost",5400);
        users = getUserName()+';';
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    ObjectOutputStream op1 = new ObjectOutputStream(socket1.getOutputStream());
                    sendMessage(socket1,new BufferedReader(new InputStreamReader(System.in)),getUserName(),op1);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            public void sendMessage(Socket socket, BufferedReader in, String name, ObjectOutputStream op)throws IOException{
                op.writeObject(new Message(name,null,null,0,3));
                op.flush();
                System.out.println("Client closed");
            }
        }).start();
        new Thread(() -> {
            try{
                while(true){
                    ObjectInputStream oi1 = new ObjectInputStream(socket1.getInputStream());
                    Message_Request m = (Message_Request) oi1.readObject();
                    for(int j=0;j< m.k;j++){
                        friendTA.appendText(m.s[j]+"\n");
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }).start();
    }
    public void lbtg(ActionEvent actionEvent) throws Exception {
        groupname = groupTF.getText();
    }
    public void lbtn(ActionEvent actionEvent) throws Exception {
        users = users+ friendTF.getText()+";";
    }
    public void lbtd(ActionEvent actionEvent) throws Exception {
        Socket socket = new Socket("localhost",5400);
        ObjectOutputStream op = new ObjectOutputStream(socket.getOutputStream());
        op.writeObject(new Message(groupname,users,null,0,8));
        op.flush();
        op.close();
    }
}
