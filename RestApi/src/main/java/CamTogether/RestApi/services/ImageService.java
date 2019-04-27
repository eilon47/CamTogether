package CamTogether.RestApi.services;


import org.springframework.stereotype.Service;
import xmls.CTImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class ImageService implements IImageService{

    @Override
    public CTImage getImage(String path) {
        return create_CTimage(path);
    }

    public static CTImage create_CTimage(String path) {
        try {
            path = "C:\\Users\\green\\Desktop\\camTogether\\CamTogether\\RestApi\\src\\main\\resources\\images\\" + path;
            CTImage img = new CTImage();
            File f = new File(path);
            BufferedImage imgg = ImageIO.read(f);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( imgg, "jpeg", baos );
            baos.flush();
            byte[] imageInByte = baos.toByteArray();

            img.setImageSize(imgg.getWidth()*imgg.getHeight());
            img.setImageName(f.getName());
            img.setImageData(imageInByte);
            // img.setAlbumName("testingAlbum");
            img.setUserName(common.IdGen.generate(imgg.toString()));
            img.setImageHeight(imgg.getHeight());
            img.setImageWidth(imgg.getWidth());
            img.setUserName("dandan");
            return img;

        }catch (IOException e) {
            return null;
        }
    }


}
