package com.jysdev.chatbot.api;

import com.jysdev.chatbot.dto.gyeonggibusapi.searchStation.Response;
import com.jysdev.chatbot.handler.GyeongGiBusStationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GyeongGiBusApiTest {

    private GyeongGiBusStationHandler gyeongGiBusStationHandler;

    @BeforeEach
    void init() {
        try {
            Map<String, Object> propMap = new Yaml().load(new FileReader("src/main/resources/key.yml"));
            Map<String, Object> propMap2 = (Map) propMap.get("gyeonggi");
            Map<String, Object> propMap3 = (Map) propMap2.get("bus");
            gyeongGiBusStationHandler = new GyeongGiBusStationHandler(propMap3.get("stationInfoSecret").toString());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("정류소 검색 테스트")
    void 경기_정류소검색_테스트() {
        // given
        String searchStationName = "신중동";

        // when
        Response response = gyeongGiBusStationHandler.busStationSearch(searchStationName);

        // then
        assertNotNull(response);
    }

    @Test
    @DisplayName("API CAll 테스트")
    void 경기_API_CALL_테스트() {
        // given
        String searchStationName = "신중동";

        // when
        String msgBody = gyeongGiBusStationHandler.busStationInquireApiCall(searchStationName);

        // then
        assertNotNull(msgBody);
    }


    @Test
    @DisplayName("검색 데이터 파싱 테스트")
    void 경기_데이터_파싱_테스트() throws IOException, JAXBException {

        // given
        FileInputStream fileInputStream = new FileInputStream("src/test/java/com/jysdev/chatbot/data/api_test_data.xml");

        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        Stream<String> streamOfString= new BufferedReader(inputStreamReader).lines();
        String streamToString = streamOfString.collect(Collectors.joining());


        // when
        Response response = gyeongGiBusStationHandler.requestXmlToEntity(streamToString);
        fileInputStream.close();

        // then
        assertNotNull(response);
        assertNotNull(response.getMsgBody().getBusStationLists());
        assertEquals(10, response.getMsgBody().getBusStationLists().length);
    }

}
