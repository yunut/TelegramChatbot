package com.jysdev.chatbot.dto.gyeonggibusapi.searchStation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
@Getter
@Setter
@ToString
public class Response {

    private String comMsgHeader;
    private MsgHeader msgHeader;
    private MsgBody msgBody;
}
