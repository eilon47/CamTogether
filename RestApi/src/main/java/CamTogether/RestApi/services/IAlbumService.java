package CamTogether.RestApi.services;


import xmls.AlbumsList;
import xmls.CTAlbum;

public interface IAlbumService {

    CTAlbum getAlbum(String userName, String albumName);
    AlbumsList getAlbums(String userName);
    String postAlbum(String userName, CTAlbum ctAlbum);

}
