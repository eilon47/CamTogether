package sample;

import javafx.scene.control.ListView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by green on 3/17/2019.
 */
public class CTClient {

    private ListView<String> albums;

    private int port;
    private String ip;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public CTClient() throws IOException {
        this.port = 23456;
        this.ip = "192.168.43.202";
    }

    public void createConnection() throws IOException {
        InetAddress addr = InetAddress.getByName(this.ip);
        this.socket = new Socket(addr, port);
        this.dis = new DataInputStream(this.socket.getInputStream());
        this.dos = new DataOutputStream(this.socket.getOutputStream());
    }

    public void sendMessage(String msg) throws IOException{
        System.out.print("Sending : " + msg);
        this.dos.writeUTF(msg);
    }

    public String getMessage() throws IOException {
        String received = this.dis.readUTF();
        System.out.print("Received : " + received);
        return received;
    }

    public int getInt() throws IOException {
        return this.dis.readInt();
    }
    public void closeConnection() throws IOException{
        this.dis.close();
        this.dos.close();
        this.socket.close();
    }

    public  static void main(String[] a) throws IOException {
        CTClient c = new CTClient();
        c.createConnection();
    }
}
