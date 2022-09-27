package com.jysdev.chatbot.dto.kakao.api.map;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class SameName{
    public String keyword;
    public ArrayList<Object> region;
    public String selected_region;
}
