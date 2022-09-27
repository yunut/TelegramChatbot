package com.jysdev.chatbot.dto.odsay.api.searchdirections;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class Result {
    private int searchType;
    private int outTrafficCheck;
    private int busCount;
    private int subwayCount;
    private int subwayBusCount;
    private int pointDistance;
    private int startRadius;
    private int endRadius;
    private ArrayList<Path> path;
}
