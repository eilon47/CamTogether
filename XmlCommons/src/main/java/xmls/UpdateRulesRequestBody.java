//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.05.27 at 08:14:13 PM IDT 
//


package xmls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NewRules" type="{xsd}Rules"/&gt;
 *         &lt;element name="Album" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "newRules",
    "album"
})
@XmlRootElement(name = "UpdateRulesRequestBody")
public class UpdateRulesRequestBody {

    @XmlElement(name = "NewRules", required = true)
    protected Rules newRules;
    @XmlElement(name = "Album", required = true)
    protected String album;

    /**
     * Gets the value of the newRules property.
     * 
     * @return
     *     possible object is
     *     {@link Rules }
     *     
     */
    public Rules getNewRules() {
        return newRules;
    }

    /**
     * Sets the value of the newRules property.
     * 
     * @param value
     *     allowed object is
     *     {@link Rules }
     *     
     */
    public void setNewRules(Rules value) {
        this.newRules = value;
    }

    /**
     * Gets the value of the album property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Sets the value of the album property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlbum(String value) {
        this.album = value;
    }

}
