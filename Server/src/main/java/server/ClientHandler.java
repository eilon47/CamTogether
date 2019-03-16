package server;

//import xsd.CommandRequest;

import handlers.MessageHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//import handlers.NewPhotoCommandHandler;

// ClientHandler class
class ClientHandler extends Thread {
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
        while (true) {
            try {
                // receive the answer from client
                received = dis.readUTF();
                logger.debug("Client handler received message: [\n" +received+"]\n");
                String response = messageHandler.messageReceived(received);
                dos.writeUTF(response);
                logger.debug("Client handler sent response: [\n" +response+"]\n");
                if (response.isEmpty())
                    break;
            } catch (IOException e) {
                logger.warn("Exception thrown", e);
            }
        }
        try {
            // closing resources
            this.dis.close();
            this.dos.close();
            this.s.close();

        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("Exception thrown", e);
        }
    }

}