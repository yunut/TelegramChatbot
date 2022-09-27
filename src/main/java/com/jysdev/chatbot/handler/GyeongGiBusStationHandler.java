package com.jysdev.chatbot.handler;

import com.jysdev.chatbot.dto.gyeonggibusapi.searchStation.BusArriveInfo;
import com.jysdev.chatbot.dto.gyeonggibusapi.searchStation.BusStationInfo;
import com.jysdev.chatbot.dto.gyeonggibusapi.searchStation.BusStationRouteInfo;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GyeongGiBusStationHandler {

    private final static Logger logger = LoggerFactory.getLogger(GyeongGiBusStationHandler.class);

    private final String stationInfoSecret;
    private String stationSearchApiURL = "http://apis.data.go.kr/6410000/busstationservice/getBusStationList";
    private String busRouteSearchApiURL = "http://apis.data.go.kr/6410000/busstationservice/getBusStationViaRouteList";

    private String busArriveTimeSearchApiURL = "http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList";

    public GyeongGiBusStationHandler(@Value("${gyeonggi.bus.stationInfoSecret}") String stationInfoSecret ) {
        this.stationInfoSecret = stationInfoSecret;
    }




    /**
     * 검색된 정류소의 최상단 정류소 정보를 반환한다.
     * TODO 검색된 정류소가 여러개인 경우 조건을 지정해 어떻게 출력해야 될지 정해야한다.
     * @param stationName
     * @return
     */
    public BusStationInfo busStationSearch(String stationName) {
        String msgBody = busStationSearchApiCall(stationName);
        Response response = requestXmlToEntity(msgBody);

        if(response.getMsgBody().getBusStationInfos().length > 0) return response.getMsgBody().getBusStationInfos()[0];

        return null;
    }

    /**
     * 버스 정류소 노선 검색
     * TODO 서울, 경기, 인천 지역에따라 관리하는 노선 API가 다르다. 추후 분기처리가 필요하다.
     * @param stationId
     * @return
     */
    public BusStationRouteInfo[] busRouteNumberSearch(long stationId) {
        String msgBody = busRouteNumberByStationIdApiCall(stationId);
        Response response = requestXmlToEntity(msgBody);

        return response.getMsgBody().getBusStationRouteInfos();
    }

    public BusArriveInfo[] busArriveInfoSearch(long stationId) {
        String msgBody = busArriveTimeApiCall(stationId);
        Response response = requestXmlToEntity(msgBody);

        return response.getMsgBody().getBusArriveInfo();
    }

    /**
     * 버스 정류소 검색 API CALL
     * @param stationName
     * @return
     */
    public String busStationSearchApiCall(String stationName) {
        try {

            URI uri = UriComponentsBuilder.fromUriString(stationSearchApiURL)
                    .queryParam("serviceKey", stationInfoSecret)
                    .queryParam("keyword", stationName)
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> rawResult
                    = restTemplate.getForEntity(uri, String.class);

            //TODO 헤더 응답 코드에 따라 핸들링하는 로직 필요



            return rawResult.getBody();

        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 버스 정류소 노선 검색 API CALL
     * @param stationId
     * @return
     */
    public String busRouteNumberByStationIdApiCall(long stationId) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(busRouteSearchApiURL)
                    .queryParam("serviceKey", stationInfoSecret)
                    .queryParam("stationId", stationId)
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> rawResult
                    = restTemplate.getForEntity(uri, String.class);

            return rawResult.getBody();

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    public String busArriveTimeApiCall(long stationId) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(busArriveTimeSearchApiURL)
                    .queryParam("serviceKey", stationInfoSecret)
                    .queryParam("stationId", stationId)
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> rawResult
                    = restTemplate.getForEntity(uri, String.class);

            return rawResult.getBody();

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    /**
     * 출발지에서 목적지 정류소까지 운행하는 노선 번호를 구한다.
     * @param startBusStationRouteInfos
     * @param arraivalStationRouteInfos
     * @return
     */
    public List<BusStationRouteInfo> targetBusNumberList(BusStationRouteInfo[] startBusStationRouteInfos, BusStationRouteInfo[] arraivalStationRouteInfos) {
        Set<Long> checkBusNumberSet = new HashSet<>();
        List<BusStationRouteInfo> possibleBusNumber = new ArrayList<>();
        for(BusStationRouteInfo busStationRouteInfo : startBusStationRouteInfos) {
            checkBusNumberSet.add(busStationRouteInfo.getRouteId());
        }

        for(BusStationRouteInfo busStationRouteInfo : arraivalStationRouteInfos) {
            if(checkBusNumberSet.contains(busStationRouteInfo.getRouteName())) {
                possibleBusNumber.add(busStationRouteInfo);
            }
        }
        return possibleBusNumber;
    }

    /**
     * XML TO Entity
     * @param xml
     * @return
     */
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
