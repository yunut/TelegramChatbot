package com.jysdev.chatbot.dto.telegram.message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class WayPoint {

    private String type;
    private String typeInfo;
    private int time;
    private String boardingStation;
    private String stopoverStation;
    private int stationCount;
    private String way;

}
