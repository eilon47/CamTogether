package CamTogether.RestApi.services;


import org.springframework.http.ResponseEntity;
import xmls.AlbumsList;
import xmls.CTAlbum;
import xmls.RequestHeader;
import xmls.Rules;

public interface IAlbumService {

    ResponseEntity<CTAlbum> getAlbum(RequestHeader header, String albumName);
    ResponseEntity<AlbumsList> getAlbums(RequestHeader header);
    ResponseEntity<String> postAlbum(RequestHeader header, CTAlbum ctAlbum);
    ResponseEntity<String> addUserToAlbum(RequestHeader header, String userToAdd, String album);
    ResponseEntity<String> updateRules(RequestHeader header, String album, Rules rules);

}
