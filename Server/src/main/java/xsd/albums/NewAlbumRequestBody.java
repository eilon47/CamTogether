package xsd.albums;
import xsd.BodyType;
import xsd.Rules;

import javax.xml.bind.annotation.*;

@XmlType(
        name = "NewAlbumRequestBody",
        propOrder = {"albumName", "userId", "rules"}
)
@XmlAccessorType(XmlAccessType.FIELD)
public class NewAlbumRequestBody extends BodyType {
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
            name = "rules",
            required = true
    )
    protected Rules rules;

    public NewAlbumRequestBody() {
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

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }
}
