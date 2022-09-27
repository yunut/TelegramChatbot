package com.jysdev.chatbot.dto.odsay.api.searchdirections;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Info{
    private int trafficDistance;
    private int totalWalk;
    private int totalTime;
    private int payment;
    private int busTransitCount;
    private int subwayTransitCount;
    private String mapObj;
    private String firstStartStation;
    private String lastEndStation;
    private int totalStationCount;
    private int busStationCount;
    private int subwayStationCount;
    private int totalDistance;
    private int totalWalkTime;
}
