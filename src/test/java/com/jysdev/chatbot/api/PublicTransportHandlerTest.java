package com.jysdev.chatbot.api;

import com.jysdev.chatbot.dto.kakao.api.map.kakaoMapApiRoot;
import com.jysdev.chatbot.dto.odsay.api.searchdirections.OdsaySearchDirectionApiRoot;
import com.jysdev.chatbot.handler.PublicTransportHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PublicTransportHandlerTest {

    private PublicTransportHandler publicTransportHandler;


    @BeforeEach
    void init() {
        try {
            Map<String, Object> propMap = new Yaml().load(new FileReader("src/main/resources/key.yml"));
            Map<String, Object> propMap2 = (Map) propMap.get("kakao");

            Map<String, Object> propMap3 = (Map) propMap.get("odsay");
            publicTransportHandler = new PublicTransportHandler(propMap2.get("secretKey").toString(),propMap3.get("secretKey").toString());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void 카카오_키워드검색_API_테스트() {
        //given
        String keyword = "신중동";

        //when
        kakaoMapApiRoot root = publicTransportHandler.kakaoKeywordSearchPlaceApiCall(keyword);

        //then
        assertNotNull(root);
    }

    @Test
    void 오디세이_대중교통_길찾기_API_테스트() {
        //given
        String sX = "126.776897204578";
        String sY = "37.5029525402622";
        String eX = "126.846042968621";
        String eY = "37.5491220803386";

        //when
        OdsaySearchDirectionApiRoot odsaySearchDirectionApiRoot = publicTransportHandler.odsayPublicTransportdirectionApiCall(sX, sY, eX, eY);

        assertNotNull(odsaySearchDirectionApiRoot);
    }

    @Test
    void 대중교통_길찾기_테스트() {
       String message = publicTransportHandler.publicStationDirection("신중동역", "보라매역");
       System.out.println(message);
    }
}
