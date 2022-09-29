package com.jysdev.chatbot.dto.telegram.message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
@Builder
public class PublicStationDirectionMessage {

    private String type;
    private long totalTime;
    private long totalPay;
    private long subwayTransitCount;
    private long busTransitCount;
    private ArrayList<WayPoint> wayPoints;
}
