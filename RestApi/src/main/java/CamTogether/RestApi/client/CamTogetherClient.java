package CamTogether.RestApi.client;

import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

@Service
public class CamTogetherClient {

    private int port;
    private String ip;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public CamTogetherClient(){
        this.ip = "0.0.0.0";
        this.port = 23456;
    }

    public CamTogetherClient(int port, String ip) {
        this.port = port;
        this.ip = ip;
    }
    public void createConnection() throws IOException {
        InetAddress addr = InetAddress.getByName(this.ip);
        this.socket = new Socket(addr, port);
        this.dis = new DataInputStream(this.socket.getInputStream());
        this.dos = new DataOutputStream(this.socket.getOutputStream());
    }

    public void sendMessage(String msg) throws IOException{
        String[] splitted = split(msg, 2048);
        sendIntMessage(splitted.length);
        for(String s: splitted)
            dos.writeUTF(s);
    }

    public void sendIntMessage(int msg) throws IOException {
        this.dos.write(msg);
    }

    public void sendMessage(byte[] msg) throws IOException{
        String s = new String(msg);
        sendMessage(s);
    }

    public void sendMessage(int message) throws IOException {
        this.dos.writeInt(message);
    }

    public int getIntResult() throws IOException {
        return this.dis.readInt();
    }

    public String getMessage() throws IOException {
        StringBuilder received = new StringBuilder();
        int numOfBlocks = getIntResult();
        for(int i=0; i < numOfBlocks; i++)
            received.append(dis.readUTF());
        return received.toString();
    }

    public void closeConnection() throws IOException{
        this.dis.close();
        this.dos.close();
        this.socket.close();

    }
    private String[] split(String str, int blockSize){
        int len = str.length();
        int size = len/blockSize + 1;
        String[] arr = new String[size];
        int i=0;
        for(i=0; i < size-1; i++){
            arr[i] = str.substring(i*blockSize, i*blockSize+blockSize);
        }
        arr[size-1] = str.substring(i*blockSize, len);
        return arr;
    }

}
