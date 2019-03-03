package xsd.albums;

import xsd.BodyType;
;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(
        name = "NewAlbumResponseBody",
        propOrder = {"albumName", "userId", "succeeded"}
)
@XmlAccessorType(XmlAccessType.FIELD)
public class NewAlbumResponseBody extends BodyType {

    @XmlElement(
            name = "albumName",
            required = true
    )
    protected String albumName;
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

    public NewAlbumResponseBody() {
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }
}
