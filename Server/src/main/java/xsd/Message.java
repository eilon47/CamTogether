package xsd;


import xsd.albums.NewAlbumRequestBody;
import xsd.albums.NewAlbumResponseBody;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.io.StringWriter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({BodyType.class, Header.class, Rules.class, NewAlbumRequestBody.class, NewAlbumResponseBody.class})
@XmlType(
        name = "",
        propOrder = {"header", "body"}
)
@XmlRootElement(
        name = "Message"
)
public class Message implements Serializable {
    @XmlElement(
            name = "header",
            required = true
    )
    protected Header header;
    @XmlElement(
            name = "body",
            required = true
    )
    protected BodyType body;


    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public BodyType getBody() {
        return body;
    }

    public void setBody(BodyType body) {
        this.body = body;
    }

    public static void main(String args[]){
        try {
            JAXBContext jaxbCtx = JAXBContext.newInstance(Message.class);
            Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            Header header = new Header();
            header.setFromUserId("eilon");
            header.setCommand("fkdl");

            NewAlbumRequestBody requestBody = new NewAlbumRequestBody();
            requestBody.setUserId("dsvdf");
            requestBody.setAlbumName("cdscdscds");
            requestBody.setRules(new Rules());

            Message msg = new Message();
            msg.setHeader(header);
            msg.setBody(requestBody);

            StringWriter sw = new StringWriter();
            marshaller.marshal(msg, sw);
            System.out.println(sw.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
