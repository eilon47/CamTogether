package CamTogether.RestApi.services;


import xmls.CTImage;

public interface IImageService {

    CTImage getImage(String image, String album, String username);
}
