package com.jysdev.chatbot.dto.kakao.api.map;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class kakaoMapApiRoot {
    public ArrayList<Document> documents;
    public Meta meta;
}
