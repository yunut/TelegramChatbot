package com.jysdev.chatbot.dto.gyeonggibusapi.searchStation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString
public class MsgHeader {

    @XmlElement(name = "queryTime")
    private String queryTime;

    @XmlElement(name = "resultCode")
    private int resultCode;

    @XmlElement(name = "queryMessage")
    private String resultMessage;
}
