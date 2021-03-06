//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.06.21 at 04:01:49 PM IDT 
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
 *     &lt;enumeration value="UPDATE_ALBUM_RULES"/&gt;
 *     &lt;enumeration value="UPDATE_USER_PROFILE"/&gt;
 *     &lt;enumeration value="CREATE_NEW_USER"/&gt;
 *     &lt;enumeration value="LOGIN_WITH_USER"/&gt;
 *     &lt;enumeration value="ADD_USER_TO_ALBUM"/&gt;
 *     &lt;enumeration value="ADD_NEW_PHOTO_TO_ALBUM"/&gt;
 *     &lt;enumeration value="GET_ALBUMS_LIST"/&gt;
 *     &lt;enumeration value="GET_ALBUM"/&gt;
 *     &lt;enumeration value="CREATE_NEW_ALBUM"/&gt;
 *     &lt;enumeration value="GET_IMAGE"/&gt;
 *     &lt;enumeration value="GET_USER_DETAILS"/&gt;
 *     &lt;enumeration value="ADD_FRIEND"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CommandsEnum")
@XmlEnum
public enum CommandsEnum {

    UPDATE_ALBUM_RULES,
    UPDATE_USER_PROFILE,
    CREATE_NEW_USER,
    LOGIN_WITH_USER,
    ADD_USER_TO_ALBUM,
    ADD_NEW_PHOTO_TO_ALBUM,
    GET_ALBUMS_LIST,
    GET_ALBUM,
    CREATE_NEW_ALBUM,
    GET_IMAGE,
    GET_USER_DETAILS,
    ADD_FRIEND;

    public String value() {
        return name();
    }

    public static CommandsEnum fromValue(String v) {
        return valueOf(v);
    }

}
