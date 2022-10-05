package com.jysdev.chatbot.handler;

import com.jysdev.chatbot.dto.kakao.api.map.kakaoMapApiRoot;
import com.jysdev.chatbot.dto.seoul.api.subway.arrive.RealtimeArrivalList;
import com.jysdev.chatbot.dto.seoul.api.subway.arrive.SeoulSubwayArriveInfo;
import com.jysdev.chatbot.dto.telegram.message.PublicStationDirectionMessage;
import com.jysdev.chatbot.dto.telegram.message.WayPoint;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@Component
public class SeoulSubwayInfoHandler {

    private String seoulDataSecrectKey;

    private String kakaoKeywordSearchPlaceURL= "http://swopenAPI.seoul.go.kr/api/subway/";

    public SeoulSubwayInfoHandler(@Value("${seoul.secretKey}") String seoulDataSecrectKey) {
        this.seoulDataSecrectKey = seoulDataSecrectKey;
    }

    public String seoulSubwayArriveInfo(String stationName) {
        SeoulSubwayArriveInfo seoulSubwayArriveInfo = seoulSubwayArriveInfoApiCall(stationName);
        String message = messageManufacture(seoulSubwayArriveInfo);
        return message;
    }
    public SeoulSubwayArriveInfo seoulSubwayArriveInfoApiCall(String stationName) {
        URI uri = UriComponentsBuilder.fromUriString(kakaoKeywordSearchPlaceURL)
                .pathSegment(seoulDataSecrectKey)
                .pathSegment("json")
                .pathSegment("realtimeStationArrival")
                .pathSegment("0")
                .pathSegment("5")
                .pathSegment(stationName)
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>("", headers);



        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SeoulSubwayArriveInfo> rawResult
                = restTemplate.exchange(uri, HttpMethod.GET, entity, SeoulSubwayArriveInfo.class);

        return rawResult.getBody();
    }

    public String messageManufacture(SeoulSubwayArriveInfo seoulSubwayArriveInfo) {

        String total_message = "";
        total_message = String.format("*%s역 열차 현황*\n", seoulSubwayArriveInfo.realtimeArrivalList.get(0).statnNm);

        for(RealtimeArrivalList arrival : seoulSubwayArriveInfo.getRealtimeArrivalList()) {
            total_message += String.format("```");
            total_message += String.format("%s / %s\n", arrival.trainLineNm, arrival.arvlMsg2);
            total_message += String.format("```");
        }

        return total_message;
    }

}
