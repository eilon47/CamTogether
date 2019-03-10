package xsd.AddNewImage;


import xsd.BodyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class AddNewImageResponseBody extends BodyType {

    @XmlElement(
            name = "userId",
            required = true
    )
    protected String userId;
    @XmlElement(
            name = "succeeded",
            required = true
    )
    protected boolean succeeded;

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
