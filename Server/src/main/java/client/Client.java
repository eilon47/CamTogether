package client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;

// Client class
public class Client
{

    protected static Logger logger = LogManager.getLogger("client");
    protected int port;
    protected String ip;
    protected Socket socket;
    protected DataInputStream dis;
    protected DataOutputStream dos;
    public Client(int port, String ip) {
        this.port = port;
        this.ip = ip;
    }

    public void createConnection() throws IOException{
        InetAddress addr = InetAddress.getByName(this.ip);
        this.socket = new Socket(addr, port);
        this.dis = new DataInputStream(this.socket.getInputStream());
        this.dos = new DataOutputStream(this.socket.getOutputStream());
    }

    public void sendMessage(String msg) throws IOException{
        logger.debug("Sending : " + msg);
        this.dos.writeUTF(msg);
        this.dos.flush();

    }

    public void sendMessage(byte[] msg) throws IOException{
        logger.debug("Sending : " + msg.length + " bytes");
        this.dos.write(msg);
        this.dos.flush();
    }

    public void sendMessage(int n) throws IOException {
        logger.debug("Sending : " + n + " as a number");
        this.dos.writeInt(n);
        this.dos.flush();

    }

    public int getIntResult() throws IOException {
        int received = this.dis.readInt();
        logger.debug("Received : " + received);
        return received;
    }

    public String getMessage() throws IOException {
        String received = this.dis.readUTF();
        logger.debug("Received : " + received);
        return received;
    }

    public void closeConnection() throws IOException{
        this.dis.close();
        this.dos.close();
        this.socket.close();

    }

}