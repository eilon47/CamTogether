package CamTogether.RestApi.services;


import xmls.CTImage;

public interface IImageService {

    String postImage(CTImage image);
    CTImage getImage(String image, String album, String username);
}
