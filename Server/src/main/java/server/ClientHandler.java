package server;

import commands.CommandRequest;
import commands.CommandResponse;
import commands.CommandsEnum;
import handlers.ICommandHandler;
import handlers.NewPhotoCommandHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// ClientHandler class
class ClientHandler extends Thread
{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    private static Logger logger = LogManager.getLogger();

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run()
    {
        logger.info("Client handler start handling...");
        String received;
        while (true)
        {
            try {
                // receive the answer from client
                received = dis.readUTF();
                CommandRequest request = CommandRequest.fromString(received);
                ICommandHandler handler = getHandler(request.getCmd());
                if(request.getCmd() == CommandsEnum.EXIY || handler == null)
                {
                    logger.info("Client " + this.s + " sends exit... or not such command");
                    logger.info("Closing this connection.");
                    this.s.close();
                    logger.info("Connection closed");
                    break;
                }
                // pass to the right handler - receive CommandResponse
                CommandResponse response = handler.handle(request);
                dos.writeUTF(response.toString());
            } catch (IOException e) {
                logger.warn("Exception thrown", e);
            }
        }
        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
            logger.warn("Exception thrown", e);
        }
    }

    private ICommandHandler getHandler(CommandsEnum cmd){ ;
        switch (cmd){
            case EXIY:
                return null;
            case NEW_PHOTO:
                    return new NewPhotoCommandHandler();
            case NEW_ALBUM:
                break;
            case ADD_NEW_USER:
                break;
            case REMOVE_ALBUM:
                return null;

        }
        return null;
    }
}