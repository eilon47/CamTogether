package client;

import xmls.CTImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CVClient extends Client {

    private String BLURRED = "400";
    private String SELFIE = "500";
    private  String OK = "0";

    public CVClient() {
        super(19090, "0.0.0.0");
    }

    public boolean queryCvServer(CTImage image, String command){
        if(command == null)
            return true;
        String byteRes = "";
        try {
            this.createConnection();
            logger.info("querying cv server for image " + image.getImageName());
            this.sendMessage(command);
            byte[] data = image.getImageData();
            sendImage(data);
            int res = readResult();
            sendMessage(res);
            String reason = readReason();
            if(res != 0)
                logger.warn("Received " + Integer.toString(res) + " Reason: " + reason);
            logger.debug("answer for image " + image.getImageName() + " is " + byteRes);
            this.closeConnection();
        }catch (IOException ex){
            logger.warn("failed to query cv server, returning default answer", ex);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return byteRes.equals(OK);
    }

    private String readReason() throws IOException {
        byte[] reason = new byte[2048];
        int read = this.dis.read(reason);
        return new String(reason);
    }

    private int readResult() throws IOException {
        byte[] res = new byte[24];
        int read = this.dis.read(res);
        System.out.println("Read result " + new String(res));
        return Integer.valueOf((new String(res)).trim());
    }

    private void sendImage(byte[] img) throws IOException, InterruptedException {
        List<byte[]> splitted = split(img, 2048);
        String size = Integer.toString(splitted.size());
        this.sendMessage(size);
        Thread.sleep(1000);
        for (byte[] aSplitted : splitted) {
            this.sendMessage(aSplitted);
        }
        System.out.println("Image sent");
    }

    private List<byte[]> split(byte[] str, int blockSize){
        int len = str.length;
        int size = len/blockSize + 1;
        List<byte[]> arr = new ArrayList<>();
        int i = 0;
        for(i=0; i < size-1; i++){
            arr.add(Arrays.copyOfRange(str, i*blockSize, i*blockSize+blockSize));
        }
        arr.add(Arrays.copyOfRange(str, i*blockSize, len));
        return arr;
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
