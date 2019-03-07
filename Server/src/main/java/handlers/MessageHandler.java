package handlers;

import database.SqlStatements;
import xsd.Message;
import commands.CommandsEnum;

import java.sql.SQLException;

public class MessageHandler {

    private CreateNewAlbumCommandHandler createNewAlbumCommandHandler = new CreateNewAlbumCommandHandler();


    public void messageReceived(Message message)  {
        CommandsEnum cmd = CommandsEnum.valueOf(message.getHeader().getCommand());
        switch (cmd){
            case NEW_ALBUM:
                createNewAlbumCommandHandler.handle(message);
        }

    }

}
