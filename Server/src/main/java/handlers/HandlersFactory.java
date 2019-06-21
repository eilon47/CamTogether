package handlers;
import xmls.CommandsEnum;
public class HandlersFactory {
    public static CommandHandler getHandler(CommandsEnum command) throws Exception{
        switch (command){
            case CREATE_NEW_ALBUM:
                return new CreateNewAlbumCommandHandler();
            case ADD_NEW_PHOTO_TO_ALBUM:
                return new NewPhotoCommandHandler();
            case ADD_USER_TO_ALBUM:
                return new AddUserToAlbumHandler();
            case GET_ALBUM:
                return new GetAlbumHandler();
            case GET_ALBUMS_LIST:
                return new GetAlbumsListHandler();
            case GET_IMAGE:
                return new GetImageHandler();
            case UPDATE_ALBUM_RULES:
                return new UpdateAlbumRulesHandler();
            case CREATE_NEW_USER:
                return new CreateNewUserHandler();
            case UPDATE_USER_PROFILE:
                return new UpdateUserProfileHandler();
            case LOGIN_WITH_USER:
                return new LoginUserHandler();
            case GET_USER_DETAILS:
                return new GetUserDetailsHandler();
            case ADD_FRIEND:
                return new AddOrGetFriendHandler();
            default:
                throw new Exception("Not implemented");
        }
    }


}
