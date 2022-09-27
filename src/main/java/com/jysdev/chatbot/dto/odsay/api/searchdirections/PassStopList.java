package com.jysdev.chatbot.dto.odsay.api.searchdirections;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class PassStopList {
    private ArrayList<Station> stations;
}
