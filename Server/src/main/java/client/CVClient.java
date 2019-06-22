package client;

import com.google.gson.Gson;
import converters.JsonConverter;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xmls.CTImage;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CVClient {
    protected static Logger logger = LogManager.getLogger("client");
    private String BLURRED = "400";
    private String SELFIE = "500";
    private String OK = "0";
    private String uri;
    private String host;
    private String port;
    private String api;

    public CVClient() {
        this.api = "analyze";
        this.port = "19090";
        this.host = "0.0.0.0";
        this.uri = "http://" + host + ":" + port + "/" + api;
    }

    public CVClient(String host, int port){
        this.port = Integer.toString(port);
        this.host = host;
        this.api = "analyze";
        this.uri = "http://" + this.host + ":" + this.port + "/" + this.api;
    }

    public CVClient(String host, int port, String api){
        this.port = Integer.toString(port);
        this.host = host;
        this.api  = api;
        this.uri = "http://" + this.host + ":" + this.port + "/" + this.api;
    }


    private boolean doPost(CTImage img, String command) {
        HashMap<String, String> map = new LinkedHashMap<>();
        map.put("command", command);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            Gson gson = new Gson();
            String json_img = gson.toJson(img);
            map.put("img", json_img);
            String json = gson.toJson(map);
            URI uri = URI.create(this.uri);
            HttpPost request = new HttpPost(uri);
            StringEntity params = new StringEntity(json);
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            byte[] res = new byte[2048];
            response.getEntity().getContent().read(res);
            String result = new String(res);
            if (!result.equals(OK)) {
                String reason;
                if(result.equals(BLURRED))
                    reason = "Image was blur";
                else if (result.equals(SELFIE))
                    reason = "Image was selfie";
                else
                    reason = "Unknown reason";
                logger.warn("Image was not passed the criterion Reason = [" + reason + "]");
                return false;
            }
            return new String(res).equals(OK);
        } catch (IOException e) {
            logger.warn(e);
        }
        return true;
    }
    public boolean queryCvServer(CTImage image, String command){
        if(command == null)
            return true;

        return doPost(image, command);
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
