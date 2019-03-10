package xsd.AddNewImage;


import xsd.BodyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlType(
        name = "ANewImageRequestBody",
        propOrder = {"image"}
)

@XmlAccessorType(XmlAccessType.FIELD)
public class AddNewImageRequestBody extends BodyType {
    @XmlElement(
            name = "album_name",
            required = true
    )
    protected String album_name;
    @XmlElement(
            name = "image_name",
            required = true
    )
    protected String image_name;
    @XmlElement(
            name = "image",
            required = true
    )
    protected byte[] image;
    public AddNewImageRequestBody(){}
    //getters
    public String getAlbum_name(){
        return album_name;
    }
    public String getImage_name(){
        return image_name;
    }
    public byte[] getData(){
        return image;
    }
    //setters
    public void setData(byte[] data) { this.image = data; }
    public void setImage_name(String img_name) { this.image_name = img_name; }
    public void setAlbum_name(String album_name) { this.album_name = album_name; }

}

