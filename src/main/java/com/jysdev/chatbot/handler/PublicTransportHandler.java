package com.jysdev.chatbot.handler;

import com.jysdev.chatbot.dto.kakao.api.map.kakaoMapApiRoot;
import com.jysdev.chatbot.dto.odsay.api.searchdirections.Lane;
import com.jysdev.chatbot.dto.odsay.api.searchdirections.OdsaySearchDirectionApiRoot;
import com.jysdev.chatbot.dto.odsay.api.searchdirections.Path;
import com.jysdev.chatbot.dto.odsay.api.searchdirections.SubPath;
import com.jysdev.chatbot.dto.telegram.message.PublicStationDirectionMessage;
import com.jysdev.chatbot.dto.telegram.message.WayPoint;
import com.vdurmont.emoji.EmojiParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class PublicTransportHandler {

    private final static Logger logger = LoggerFactory.getLogger(PublicTransportHandler.class);

    private String kakaoAPISecrectKey;
    private String odsayAPISecrectKey;

    private String kakaoKeywordSearchPlaceURL= "https://dapi.kakao.com/v2/local/search/keyword.json";
    private String odsayPublicTransportDirection = "https://api.odsay.com/v1/api/searchPubTransPathT";

    public PublicTransportHandler(@Value("${kakao.secretKey}") String kakaoAPISecrectKey, @Value("${odsay.secretKey}") String odsayAPISecrectKey) {
        this.kakaoAPISecrectKey = kakaoAPISecrectKey;
        this.odsayAPISecrectKey = odsayAPISecrectKey;
    }

    public String publicStationDirection(String start, String dest) {

        // 출발지, 목적지의 위,경도를 구함
        kakaoMapApiRoot startKakaoMapApiRoot = kakaoKeywordSearchPlaceApiCall(start);
        kakaoMapApiRoot destKakaoMapApiRoot = kakaoKeywordSearchPlaceApiCall(dest);

        // 경로 탐색
        String sX = startKakaoMapApiRoot.documents.get(0).getX();
        String sY = startKakaoMapApiRoot.documents.get(0).getY();
        String dX = destKakaoMapApiRoot.documents.get(0).getX();
        String dY = destKakaoMapApiRoot.documents.get(0).getY();
        OdsaySearchDirectionApiRoot odsaySearchDirectionApiRoot = odsayPublicTransportdirectionApiCall(sX, sY, dX, dY);

        // 추천 경로는 최대 1개 (텔레그램 채팅방 추천 경로가 2개 이상 일 시 알아보기 힘든 문제가 있음)
        ArrayList<PublicStationDirectionMessage> message = new ArrayList<>();
        for(Path path : odsaySearchDirectionApiRoot.getResult().getPath()) {

            // 경유지 가공
            ArrayList<WayPoint> wayPoints = new ArrayList<>();
            for(SubPath subPath : path.getSubPath()) {

                //교통 수단 정보 확장 노드 분석
                String typeInfo = null;
                if(subPath.getLane() != null) {
                    for(Lane lane : subPath.getLane()) {
                        if (lane.getName() != null) {
                            typeInfo = lane.getName();
                        } else if (lane.getBusNo() != null) {
                            typeInfo = lane.getBusNo();
                        }
                        break;
                    }
                }

                wayPoints.add(WayPoint.builder()
                        .type(TrafficType.values()[subPath.getTrafficType()].getName())
                        .typeInfo(typeInfo)
                        .time(subPath.getSectionTime())
                        .boardingStation(subPath.getStartName())
                        .stopoverStation(subPath.getEndName())
                        .stationCount(subPath.getStationCount())
                        .way(subPath.getWay())
                        .build());
            }

            // 메시지 빌드
            message.add(PublicStationDirectionMessage.builder()
                    .type(PathType.values()[path.getPathType()].getName())
                    .totalTime(path.getInfo().getTotalTime())
                    .totalPay(path.getInfo().getPayment())
                    .busTransitCount(path.getInfo().getBusTransitCount())
                    .subwayTransitCount(path.getInfo().getSubwayTransitCount())
                    .wayPoints(wayPoints)
                    .build());
            break;
        }

        String telegram_message = messageManufacture(message, start, dest);

        return telegram_message;
    }

    public kakaoMapApiRoot kakaoKeywordSearchPlaceApiCall(String keyword) {
        URI uri = UriComponentsBuilder.fromUriString(kakaoKeywordSearchPlaceURL)
                .queryParam("query", keyword)
                .queryParam("sort", "accuracy")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "KakaoAK " + kakaoAPISecrectKey);

        HttpEntity<String> entity = new HttpEntity<String>("", headers);



        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<kakaoMapApiRoot> rawResult
                = restTemplate.exchange(uri, HttpMethod.GET, entity, kakaoMapApiRoot.class);

        return rawResult.getBody();
    }

    public OdsaySearchDirectionApiRoot odsayPublicTransportdirectionApiCall(String sX, String sY, String eX, String eY) {
        URI uri = UriComponentsBuilder.fromUriString(odsayPublicTransportDirection)
                .queryParam("apiKey", odsayAPISecrectKey)
                .queryParam("lang", 0)
                .queryParam("OPT", 0)
                .queryParam("SX", sX)
                .queryParam("SY", sY)
                .queryParam("EX", eX)
                .queryParam("EY", eY)
                .encode()
                .build()
                .toUri();
        System.out.println(uri.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>("", headers);


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<OdsaySearchDirectionApiRoot> rawResult
                = restTemplate.exchange(uri, HttpMethod.GET, entity, OdsaySearchDirectionApiRoot.class);

        return rawResult.getBody();
    }

    public String messageManufacture(ArrayList<PublicStationDirectionMessage> messages, String start, String dest) {

        String total_message = new String();
        total_message = String.format("*%s에서 %s까지 추천경로*\n", start, dest);

        for(PublicStationDirectionMessage message : messages) {
            total_message += String.format("총 시간: %s분\n", message.getTotalTime());
            total_message += String.format("총 비용: %s원\n", message.getTotalPay());
            total_message += String.format("환승: 지하철: %s번 / 버스: %s번\n", message.getSubwayTransitCount(), message.getBusTransitCount());

            for(WayPoint wayPoint : message.getWayPoints()) {
                String type = wayPoint.getType();
                if(type.equals(TrafficType.SUBWAY.getName())) {
                    total_message += String.format("```");
                    total_message += String.format("ㅇ " + EmojiParser.parseToUnicode(":train2:" + " %s역 \\-\\> %s역(%s)\n"), wayPoint.getBoardingStation(), wayPoint.getStopoverStation(), wayPoint.getTypeInfo().replaceAll("수도권", "").trim());
                    total_message += String.format(" \\- %s정거장 후 하차 / 소요시간: %s분\n\n", wayPoint.getStationCount(), wayPoint.getTime());

                    total_message += String.format("```");
                } else if(type.equals(TrafficType.BUS.getName())) {
                    total_message += String.format("```");
                    total_message += String.format("ㅇ "+ EmojiParser.parseToUnicode(":bus: %s \\-\\> %s\n"), wayPoint.getTypeInfo(), wayPoint.getBoardingStation().replace(".","\\."), wayPoint.getStopoverStation().replace(".","\\."));
                    total_message += String.format(" \\- %s정거장 후 하차 / 소요시간 %s분\n", wayPoint.getStationCount(), wayPoint.getTime());
                    total_message += String.format("```");
                } else if(type.equals(TrafficType.WALK.getName())) {
                    if(wayPoint.getTime() != 0) {
                        total_message += String.format("```");
                        total_message += String.format("ㅇ " + EmojiParser.parseToUnicode(":walking: %s분\n\n"), wayPoint.getTime());
                        total_message += String.format("```");
                    }
                }
            }
        }

        return total_message;

    }




}

enum PathType {
    DUMMY("더미"), SUBWAY("지하철"), BUS("버스"), BUSANDSUBWAY("버스+지하철");

    final private String name;
    public String getName() {
        return name;
    }
    private PathType(String name){
        this.name = name;
    }
}

enum TrafficType {
    DUMMY("더미"), SUBWAY("지하철"), BUS("버스"), WALK("도보");

    final private String name;
    public String getName() {
        return name;
    }
    private TrafficType(String name){
        this.name = name;
    }
}