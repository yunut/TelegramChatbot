package com.jysdev.chatbot.api;

import com.jysdev.chatbot.dto.seoul.api.subway.arrive.SeoulSubwayArriveInfo;
import com.jysdev.chatbot.handler.PublicTransportHandler;
import com.jysdev.chatbot.handler.SeoulSubwayInfoHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SeoulSubwayHandlerTest {

    private SeoulSubwayInfoHandler seoulSubwayInfoHandler;


    @BeforeEach
    void init() {
        try {
            Map<String, Object> propMap = new Yaml().load(new FileReader("src/main/resources/key.yml"));
            Map<String, Object> propMap2 = (Map) propMap.get("seoul");
            seoulSubwayInfoHandler = new SeoulSubwayInfoHandler(propMap2.get("secretKey").toString());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("서울 지하철 도착 API 테스트")
    void 서울_지하철_도착_API_테스트() {
        SeoulSubwayArriveInfo info = seoulSubwayInfoHandler.seoulSubwayArriveInfoApiCall("신중동");

        assertNotNull(info);
    }
}
