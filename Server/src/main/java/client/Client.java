package client;
import commands.CommandsEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;

// Client class
public class Client
{

    private static Logger logger = LogManager.getLogger();
    private int port;
    private String ip;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    public Client(int port, String ip) {
        this.port = port;
        this.ip = ip;
    }

    private void createConnection() throws IOException{
        InetAddress addr = InetAddress.getByName(this.ip);
        this.socket = new Socket(addr, port);
        this.dis = new DataInputStream(this.socket.getInputStream());
        this.dos = new DataOutputStream(this.socket.getOutputStream());
    }

    public void sendMessage(String msg) throws IOException{
        logger.info("Sending : " + msg);
        this.dos.writeUTF(msg);
    }

    public String getMessage() throws IOException {
        String received = this.dis.readUTF();
        logger.info("Received : " + received);
        return received;
    }

    public void closeConnection() throws IOException{
        this.dis.close();
        this.dos.close();
        this.socket.close();

    }

    public static void main(String args[]) {
        Client client = new Client(18080, "127.0.0.1");
        try {
            client.createConnection();
//            CommandRequest request = new CommandRequest(CommandsEnum.NEW_PHOTO, "Add photo", client.socket.toString());
//            client.sendMessage(request.toString());
//            String rec = client.getMessage();
//            CommandResponse res = CommandResponse.fromString(rec);
            client.closeConnection();
        } catch (Exception e){
            System.out.println("Exiting..");
        }
    }
}