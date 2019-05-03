package CamTogether.RestApi.services;


import xmls.AlbumsList;
import xmls.CTAlbum;
import xmls.Rules;

public interface IAlbumService {

    CTAlbum getAlbum(String userName, String albumName);
    AlbumsList getAlbums(String userName);
    String postAlbum(String userName, CTAlbum ctAlbum);
    boolean addUserToAlbum(String user,String userToAdd,  String album);
    boolean updateRules(String username, String album, Rules rules);

}
