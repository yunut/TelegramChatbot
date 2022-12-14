package com.jysdev.chatbot.dto.kakao.api.map;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Document {
    private String address_name;
    private String category_group_code;
    private String category_group_name;
    private String category_name;
    private String distance;
    private String id;
    private String phone;
    private String place_name;
    private String place_url;
    private String road_address_name;
    private String x;
    private String y;
}
