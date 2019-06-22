package client;

import com.google.gson.Gson;
import converters.JsonConverter;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.ResponseEntity;
import xmls.CTImage;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.*;

public class CVClient extends Client {

    private String BLURRED = "400";
    private String SELFIE = "500";
    private  String OK = "0";

    public CVClient() {
        super(19090, "0.0.0.0");
    }


    private String doPost(CTImage img, String command) {
        HashMap<String, String> map = new LinkedHashMap<>();
        map.put("command", command);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            Gson gson = new Gson();

            byte[] img_by = new byte[img.getImageData().length];
            for(int i=0; i< img.getImageData().length; i++)
                img_by[i] = (byte) (img.getImageData()[i] & 0xFF);
            map.put("img_bytes", new String(img_by));
            img.setImageData(null);
            String json_img = gson.toJson(img);
            map.put("img", json_img);
            String json = gson.toJson(map);
            URI uri = URI.create("http://0.0.0.0:19090/analyze");
            HttpPost request = new HttpPost(uri);
            StringEntity params = new StringEntity(json);
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            byte[] res = new byte[2048];
            response.getEntity().getContent().read(res);
            System.out.println(new String(res));
            return new String(res);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean queryCvServer(CTImage image, String command){
        if(command == null)
            return true;

        String res = doPost(image, command);
        return true;
//        String byteRes = "";
//        try {
//            this.createConnection();
//            logger.info("querying cv server for image " + image.getImageName());
//            this.sendMessage(command);
//            byte[] data = image.getImageData();
//            sendImage(data);
//            int res = readResult();
//            sendMessage(res);
//            String reason = readReason();
//            if(res != 0)
//                logger.warn("Received " + Integer.toString(res) + " Reason: " + reason);
//            logger.debug("answer for image " + image.getImageName() + " is " + byteRes);
//            this.closeConnection();
//        }catch (IOException ex){
//            logger.warn("failed to query cv server, returning default answer", ex);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return byteRes.equals(OK);
    }

    private static CTImage createCTImage(String path) throws IOException {
        //Read the JSON file
        CTImage img = new CTImage();
        File f = new File(path);
        BufferedImage imgg = ImageIO.read(f);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( imgg, "jpg", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        img.setImageData(imageInByte);
        //Iterate over this map
        img.setAlbumName("albumName");
        img.setLatitude(0);
        img.setLongitude(0);
        img.setUserName("userName");
        img.setTitle("title");
        img.setImageWidth(0);
        img.setImageHeight(0);
        img.setImageSize(0);
        img.setDate(null);
        img.setImageName("imageName");
        return img;
    }



    public static void main(String[] args) {
        String blurred = "C:\\Users\\eilon\\Desktop\\CamTogether\\Server\\src\\main\\resources\\blur.jpg";
        String selfie = "C:\\Users\\eilon\\Desktop\\CamTogether\\Server\\src\\main\\resources\\test_image.jpg";
        String manyfaces = "C:\\Users\\eilon\\Desktop\\CamTogether\\Server\\src\\main\\resources\\faces.jpg";
        CVClient client = new CVClient();
        CTImage image = new CTImage();
        JsonConverter c = new JsonConverter();
        String r = null;
        try {
            r = c.serializeToString(image);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        System.out.println(r);
        try {
            CTImage fail = createCTImage(blurred);
            CTImage fail2 = createCTImage(selfie);
            CTImage success = createCTImage(manyfaces);
            boolean f1 = client.queryCvServer(fail, "Blur");
            boolean f2 = client.queryCvServer(fail2, "All");
            boolean t = client.queryCvServer(success, "All");
            System.out.println(f1);
            System.out.println(f2);
            System.out.println(t);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
