package com.jysdev.chatbot.dto.odsay.api.searchdirections;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class Path {
    private int pathType;
    private Info info;
    private ArrayList<SubPath> subPath;
}
