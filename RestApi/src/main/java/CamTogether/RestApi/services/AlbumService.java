package CamTogether.RestApi.services;


import org.springframework.stereotype.Service;

@Service
public class AlbumService implements IAlbumService{


    @Override
    public String getAlbum(String albumName) {
        return albumName;
    }
}
