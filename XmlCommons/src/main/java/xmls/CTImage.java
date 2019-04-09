//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.04.09 at 10:09:20 PM IDT 
//


package xmls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CTImage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CTImage"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ImageName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ImageData" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="ImageSize" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ImageLength" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ImageWidth" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="AlbumName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="UserName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="UserID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CTImage", propOrder = {
    "imageName",
    "imageData",
    "imageSize",
    "imageLength",
    "imageWidth",
    "albumName",
    "userName",
    "userID"
})
public class CTImage {

    @XmlElement(name = "ImageName", required = true)
    protected String imageName;
    @XmlElement(name = "ImageData", required = true)
    protected byte[] imageData;
    @XmlElement(name = "ImageSize")
    protected int imageSize;
    @XmlElement(name = "ImageLength")
    protected int imageLength;
    @XmlElement(name = "ImageWidth")
    protected int imageWidth;
    @XmlElement(name = "AlbumName", required = true)
    protected String albumName;
    @XmlElement(name = "UserName", required = true)
    protected String userName;
    @XmlElement(name = "UserID", required = true)
    protected String userID;

    /**
     * Gets the value of the imageName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Sets the value of the imageName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImageName(String value) {
        this.imageName = value;
    }

    /**
     * Gets the value of the imageData property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getImageData() {
        return imageData;
    }

    /**
     * Sets the value of the imageData property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setImageData(byte[] value) {
        this.imageData = value;
    }

    /**
     * Gets the value of the imageSize property.
     * 
     */
    public int getImageSize() {
        return imageSize;
    }

    /**
     * Sets the value of the imageSize property.
     * 
     */
    public void setImageSize(int value) {
        this.imageSize = value;
    }

    /**
     * Gets the value of the imageLength property.
     * 
     */
    public int getImageLength() {
        return imageLength;
    }

    /**
     * Sets the value of the imageLength property.
     * 
     */
    public void setImageLength(int value) {
        this.imageLength = value;
    }

    /**
     * Gets the value of the imageWidth property.
     * 
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * Sets the value of the imageWidth property.
     * 
     */
    public void setImageWidth(int value) {
        this.imageWidth = value;
    }

    /**
     * Gets the value of the albumName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlbumName() {
        return albumName;
    }

    /**
     * Sets the value of the albumName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlbumName(String value) {
        this.albumName = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the userID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the value of the userID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserID(String value) {
        this.userID = value;
    }

}
