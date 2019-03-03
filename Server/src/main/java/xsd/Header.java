package xsd;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "Header",
        propOrder = {"command", "fromUserId"}
)
public class Header implements Serializable {
    @XmlElement(
            name = "command",
            required = true
    )
    protected String command;
    @XmlElement(
            name = "fromUserId",
            required = true
    )
    protected String fromUserId;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }
}
