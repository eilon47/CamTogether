package client;

import xmls.CTImage;

import java.io.IOException;

public class CVClient extends Client {

    public CVClient() {
        super(19090, "0.0.0.0");
    }

    public boolean queryCvServer(CTImage image){
        int byteRes = 0;
        try {
            this.createConnection();
            logger.info("querying cv server for image " + image.getImageName());
            byte[] data = image.getImageData();
            String[] splitted = split(new String(data), 2048);
            this.sendMessage(splitted.length);
            for (String aSplitted : splitted) {
                this.sendMessage(aSplitted);
            }
            byteRes = this.getIntResult();
            logger.debug("answer for image " + image.getImageName() + " is " + byteRes);
            this.closeConnection();
        }catch (IOException ex){
            logger.warn("failed to query cv server, returning default answer", ex);
        }
        return byteRes >= 0;
    }

    private String[] split(String str, int blockSize){
        int len = str.length();
        int size = len/blockSize + 1;
        String[] arr = new String[size];
        int i=0;
        for(i=0; i < size-1; i++){
            arr[i] = str.substring(i*blockSize, i*blockSize+blockSize);
        }
        arr[size-1] = str.substring(i*blockSize, len);
        return arr;
    }
}
