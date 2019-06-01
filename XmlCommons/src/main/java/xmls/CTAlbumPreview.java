//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.06.01 at 03:12:50 PM IDT 
//


package xmls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CTAlbumPreview complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CTAlbumPreview"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PreviewImg" type="{xsd}CTThumbnail"/&gt;
 *         &lt;element name="NumberOfImages" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CTAlbumPreview", propOrder = {
    "name",
    "previewImg",
    "numberOfImages"
})
public class CTAlbumPreview {

    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "PreviewImg", required = true)
    protected CTThumbnail previewImg;
    @XmlElement(name = "NumberOfImages")
    protected int numberOfImages;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the previewImg property.
     * 
     * @return
     *     possible object is
     *     {@link CTThumbnail }
     *     
     */
    public CTThumbnail getPreviewImg() {
        return previewImg;
    }

    /**
     * Sets the value of the previewImg property.
     * 
     * @param value
     *     allowed object is
     *     {@link CTThumbnail }
     *     
     */
    public void setPreviewImg(CTThumbnail value) {
        this.previewImg = value;
    }

    /**
     * Gets the value of the numberOfImages property.
     * 
     */
    public int getNumberOfImages() {
        return numberOfImages;
    }

    /**
     * Sets the value of the numberOfImages property.
     * 
     */
    public void setNumberOfImages(int value) {
        this.numberOfImages = value;
    }

}
