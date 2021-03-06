package server;

//import xsd.CommandRequest;

import handlers.MessageHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

//import handlers.NewPhotoCommandHandler;

// ClientHandler class
class ClientHandler implements Runnable {
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket s;
    private static Logger logger = LogManager.getLogger("server");
    private MessageHandler messageHandler;

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.messageHandler = new MessageHandler();
    }

    @Override
    public void run() {
        logger.info("Client handler start handling...");
        String received;
        try {
            received = readProcedure();
            logger.debug("Client handler received message: [\n" + received + "]\n");
            String response = messageHandler.messageReceived(received);
            writeProcedure(response);
            logger.debug("Client handler sent response: [\n" + response + "]\n");
            // closing resources
            this.dis.close();
            this.dos.close();
            this.s.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("Exception thrown", e);
        }
    }

    public String[] split(String str, int blockSize){
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

    private String readProcedure() throws IOException {
        logger.info("Reading!");
        int len = dis.readInt();
        logger.info("Read, " + len);
        StringBuilder sb = new StringBuilder();
        for (int i =0; i < len; i++)
            sb.append(dis.readUTF());
        return sb.toString();
    }

    private void writeProcedure(String messgae)  throws IOException {
        String[] splitted = split(messgae, 2048);
        dos.writeInt(splitted.length);
        for(String s: splitted)
            dos.writeUTF(s);
    }

}