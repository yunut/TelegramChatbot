package com.jysdev.chatbot.handler;

import com.jysdev.chatbot.dto.gyeonggibusapi.searchStation.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class GyeongGiBusStationHandler {

    private final static Logger logger = LoggerFactory.getLogger(GyeongGiBusStationHandler.class);

    private final String stationInfoSecret;
    private String apiURL = "http://apis.data.go.kr/6410000/busstationservice/getBusStationList";

    public GyeongGiBusStationHandler(@Value("${gyeonggi.bus.stationInfoSecret}") String stationInfoSecret ) {
        this.stationInfoSecret = stationInfoSecret;
    }

    // TODO 응답된 response 엔티티 중 조건에 맞는 정류장을 뽑고 타겟으로 지정 하는 메소드 추가해야한다.
    public Response busStationSearch(String stationName) {
        String msgBody = busStationInquireApiCall(stationName);
        Response response = requestXmlToEntity(msgBody);
        return response;
    }

    public String busStationInquireApiCall(String stationName) {
        try {
            // 공공데이터 주변 정류장 API 요청
            String stationEncodeURL = URLEncoder.encode(stationName, StandardCharsets.UTF_8);

            URI uri = UriComponentsBuilder.fromUriString(apiURL)
                    .queryParam("serviceKey", stationInfoSecret)
                    .queryParam("keyword", stationName)
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> rawResult
                    = restTemplate.getForEntity(uri, String.class);

            return rawResult.getBody();

        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public Response requestXmlToEntity(String xml) {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Response.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Response response = (Response) unmarshaller.unmarshal(new StringReader(xml));

            return response;
        } catch (JAXBException e) {
            logger.error(e.getMessage());
        }
        return null;
    }


}
