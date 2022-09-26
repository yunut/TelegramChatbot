package com.jysdev.chatbot.dto.gyeonggibusapi.searchStation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter
@ToString
public class MsgBody {

    @XmlElement(name = "busStationList")
    private BusStationList[] busStationLists;
}
