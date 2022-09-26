package com.jysdev.chatbot.dto.gyeonggibusapi.searchStation;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter
@ToString
public class BusStationList {

    @XmlElement(name = "centerYn")
    private String centerYn;

    @XmlElement(name = "districtCd")
    private int districtCd;

    @XmlElement(name = "mobileNo")
    private long mobileNo;

    @XmlElement(name = "regionName")
    private String regionName;

    @XmlElement(name = "stationId")
    private long stationId;

    @XmlElement(name = "stationName")
    private String stationName;

    @XmlElement(name = "x")
    private double x;

    @XmlElement(name = "y")
    private double y;
}
