package xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "Rules",
        propOrder = {"longitude", "latitude","radius" ,"start_date", "end_date","start_hour", "end_hour"}
)
public class Rules implements Serializable {
    @XmlElement(
            name = "longitude",
            required = false
    )
    float longitude;
    @XmlElement(
            name = "latitude"
    )
    float latitude;
    @XmlElement(
            name = "radius"
    )
    int radius;
    @XmlElement(
            name = "start_date"
    )
    String start_date;
    @XmlElement(
            name = "end_date"
    )
    String end_date;
    @XmlElement(
            name = "end_hour"
    )
    String start_hour;
    @XmlElement(
            name = "end_hour"
    )
    String end_hour;

    public Rules() {
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_hour() {
        return start_hour;
    }

    public void setStart_hour(String start_hour) {
        this.start_hour = start_hour;
    }

    public String getEnd_hour() {
        return end_hour;
    }

    public void setEnd_hour(String end_hour) {
        this.end_hour = end_hour;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
