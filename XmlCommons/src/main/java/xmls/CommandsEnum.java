//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.16 at 02:24:24 PM IST 
//


package xmls;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CommandsEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CommandsEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CREATE_NEW_ALBUM"/&gt;
 *     &lt;enumeration value="ADD_NEW_PHOTO"/&gt;
 *     &lt;enumeration value="GET_PHOTOS_FROM_ALBUM"/&gt;
 *     &lt;enumeration value="ADD_USER_TO_ALBUM"/&gt;
 *     &lt;enumeration value="GET_ALBUM"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CommandsEnum")
@XmlEnum
public enum CommandsEnum {

    CREATE_NEW_ALBUM,
    ADD_NEW_PHOTO,
    GET_PHOTOS_FROM_ALBUM,
    ADD_USER_TO_ALBUM,
    GET_ALBUM;

    public String value() {
        return name();
    }

    public static CommandsEnum fromValue(String v) {
        return valueOf(v);
    }

}
