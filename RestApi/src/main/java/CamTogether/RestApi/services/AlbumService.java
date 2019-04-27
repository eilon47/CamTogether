package CamTogether.RestApi.services;


import org.springframework.stereotype.Service;
import xmls.AlbumsList;
import xmls.CTAlbum;
import xmls.NewAlbumRequestBody;

@Service
public class AlbumService implements IAlbumService{

    @Override
    public AlbumsList getAlbums(String userName) {
        // here we need to return album list from server
        AlbumsList al = new AlbumsList();
        for (int i = 1; i < 8; i++) {
            al.getAlbums().add(createAlbum(userName, Integer.toString(i)));
        }
        return al;
    }

    @Override
    public CTAlbum getAlbum(String userName,String albumName) {
        // here we need to get the album from server
        CTAlbum ctAlbum = createAlbum(userName,albumName);
        return ctAlbum;
    }

    @Override
    public String postAlbum(String userName, CTAlbum ctAlbum) {
        // this request we wanna send to server
        NewAlbumRequestBody narb = new NewAlbumRequestBody();
        narb.setAlbum(ctAlbum);
        // need to return confirmation message from server
        return "Success!";
    }

    public CTAlbum createAlbum(String userName, String albumName) {
        CTAlbum ctAlbum = new CTAlbum();
        ctAlbum.setCreationDate(null);
        ctAlbum.setCreator(userName);
        ctAlbum.setDescription("Dummy Album");
        ctAlbum.setExpirationDate(null);
        ctAlbum.setName(albumName);
        ctAlbum.setRules(null);
        for (int i = 1; i < 7; i++) {
            ctAlbum.getImages().add(ImageService.create_CTimage(Integer.toString(i)+ ".jpg"));
        }
        return  ctAlbum;
    }


}
