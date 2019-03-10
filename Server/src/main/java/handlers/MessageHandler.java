package handlers;

import database.SqlStatements;
import xsd.Message;
import commands.CommandsEnum;

import javax.xml.bind.JAXBException;
import java.sql.SQLException;

public class MessageHandler {

    private CreateNewAlbumCommandHandler createNewAlbumCommandHandler = new CreateNewAlbumCommandHandler();
    private NewPhotoCommandHandler add_new_image = new NewPhotoCommandHandler();

    public void messageReceived(Message message) throws JAXBException {
        CommandsEnum cmd = CommandsEnum.valueOf(message.getHeader().getCommand());
        switch (cmd){
            case NEW_ALBUM:
                createNewAlbumCommandHandler.handle(message);
            case NEW_PHOTO:
                add_new_image.handle(message);
        }

    }

}
