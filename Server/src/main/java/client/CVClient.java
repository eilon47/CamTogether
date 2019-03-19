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
            this.sendMessage(data);
            byteRes = this.getIntResult();
            logger.debug("answer for image " + image.getImageName() + " is " + byteRes);
            this.closeConnection();
        }catch (IOException ex){
            logger.warn("failed to query cv server, returning default answer", ex);
        }
        if(byteRes < 0)
            return false;
        return true;
    }
}
