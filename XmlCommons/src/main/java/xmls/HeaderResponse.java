//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.04.13 at 07:32:42 PM IDT 
//


package xmls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HeaderResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HeaderResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Command" type="{xsd}CommandsEnum"/&gt;
 *         &lt;element name="CommandSuccess" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HeaderResponse", propOrder = {
    "userId",
    "command",
    "commandSuccess"
})
public class HeaderResponse {

    @XmlElement(name = "UserId", required = true)
    protected String userId;
    @XmlElement(name = "Command", required = true)
    @XmlSchemaType(name = "string")
    protected CommandsEnum command;
    @XmlElement(name = "CommandSuccess")
    protected boolean commandSuccess;

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * Gets the value of the command property.
     * 
     * @return
     *     possible object is
     *     {@link CommandsEnum }
     *     
     */
    public CommandsEnum getCommand() {
        return command;
    }

    /**
     * Sets the value of the command property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommandsEnum }
     *     
     */
    public void setCommand(CommandsEnum value) {
        this.command = value;
    }

    /**
     * Gets the value of the commandSuccess property.
     * 
     */
    public boolean isCommandSuccess() {
        return commandSuccess;
    }

    /**
     * Sets the value of the commandSuccess property.
     * 
     */
    public void setCommandSuccess(boolean value) {
        this.commandSuccess = value;
    }

}
