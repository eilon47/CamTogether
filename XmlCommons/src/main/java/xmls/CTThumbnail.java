//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.06.21 at 04:01:49 PM IDT 
//


package xmls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CTThumbnail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CTThumbnail"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ThumbnailData" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="ThumbnailHeight" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ThumbnailWidth" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ThumbnailName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CTThumbnail", propOrder = {
    "thumbnailData",
    "thumbnailHeight",
    "thumbnailWidth",
    "thumbnailName"
})
public class CTThumbnail {

    @XmlElement(name = "ThumbnailData", required = true)
    protected byte[] thumbnailData;
    @XmlElement(name = "ThumbnailHeight")
    protected int thumbnailHeight;
    @XmlElement(name = "ThumbnailWidth")
    protected int thumbnailWidth;
    @XmlElement(name = "ThumbnailName", required = true)
    protected String thumbnailName;

    /**
     * Gets the value of the thumbnailData property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getThumbnailData() {
        return thumbnailData;
    }

    /**
     * Sets the value of the thumbnailData property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setThumbnailData(byte[] value) {
        this.thumbnailData = value;
    }

    /**
     * Gets the value of the thumbnailHeight property.
     * 
     */
    public int getThumbnailHeight() {
        return thumbnailHeight;
    }

    /**
     * Sets the value of the thumbnailHeight property.
     * 
     */
    public void setThumbnailHeight(int value) {
        this.thumbnailHeight = value;
    }

    /**
     * Gets the value of the thumbnailWidth property.
     * 
     */
    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    /**
     * Sets the value of the thumbnailWidth property.
     * 
     */
    public void setThumbnailWidth(int value) {
        this.thumbnailWidth = value;
    }

    /**
     * Gets the value of the thumbnailName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThumbnailName() {
        return thumbnailName;
    }

    /**
     * Sets the value of the thumbnailName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThumbnailName(String value) {
        this.thumbnailName = value;
    }

}
