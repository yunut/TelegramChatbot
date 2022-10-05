package com.jysdev.chatbot.dto.seoul.api.subway.arrive;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class SeoulSubwayArriveInfo {
    public ErrorMessage errorMessage;
    public ArrayList<RealtimeArrivalList> realtimeArrivalList;
}
