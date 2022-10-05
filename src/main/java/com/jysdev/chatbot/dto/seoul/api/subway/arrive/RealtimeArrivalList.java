package com.jysdev.chatbot.dto.seoul.api.subway.arrive;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RealtimeArrivalList {
    public Object beginRow;
    public Object endRow;
    public Object curPage;
    public Object pageRow;
    public int totalCount;
    public int rowNum;
    public int selectedCount;
    public String subwayId;
    public Object subwayNm;
    public String updnLine;
    public String trainLineNm;
    public String subwayHeading;
    public String statnFid;
    public String statnTid;
    public String statnId;
    public String statnNm;
    public Object trainCo;
    public String ordkey;
    public String subwayList;
    public String statnList;
    public String btrainSttus;
    public String barvlDt;
    public String btrainNo;
    public String bstatnId;
    public String bstatnNm;
    public String recptnDt;
    public String arvlMsg2;
    public String arvlMsg3;
    public String arvlCd;
}
