package com.jysdev.chatbot.dto.odsay.api.searchdirections;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Lane {
    private String name;
    private int subwayCode;
    private int subwayCityCode;
    private String busNo;
    private int type;
    private int busID;
    private String busLocalBlID;
    private int busCityCode;
    private int busProviderCode;
}
