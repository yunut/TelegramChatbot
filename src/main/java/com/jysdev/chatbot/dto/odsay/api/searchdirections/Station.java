package com.jysdev.chatbot.dto.odsay.api.searchdirections;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Station {
    private int index;
    private int stationID;
    private String stationName;
    private String x;
    private String y;
    private int stationCityCode;
    private int stationProviderCode;
    private String localStationID;
    private String arsID;
    private String isNonStop;
}
