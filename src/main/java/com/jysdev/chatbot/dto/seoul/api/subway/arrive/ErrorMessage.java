package com.jysdev.chatbot.dto.seoul.api.subway.arrive;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ErrorMessage {
    public int status;
    public String code;
    public String message;
    public String link;
    public String developerMessage;
    public int total;
}
