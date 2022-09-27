package com.jysdev.chatbot.dto.odsay.api.searchdirections;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class SubPath {
    private int trafficType;
    private int distance;
    private int sectionTime;
    private int stationCount;
    private ArrayList<Lane> lane;
    private String startName;
    private double startX;
    private double startY;
    private String endName;
    private double endX;
    private double endY;
    private String way;
    private int wayCode;
    private String door;
    private int startID;
    private int endID;
    private String startExitNo;
    private double startExitX;
    private double startExitY;
    private String endExitNo;
    private double endExitX;
    private double endExitY;
    private PassStopList passStopList;
    private int startStationCityCode;
    private int startStationProviderCode;
    private String startLocalStationID;
    private String startArsID;
    private int endStationCityCode;
    private int endStationProviderCode;
    private String endLocalStationID;
    private String endArsID;
}
