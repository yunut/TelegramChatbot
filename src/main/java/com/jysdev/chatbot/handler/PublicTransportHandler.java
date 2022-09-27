package com.jysdev.chatbot.handler;

import com.jysdev.chatbot.dto.kakao.api.map.kakaoMapApiRoot;
import com.jysdev.chatbot.dto.odsay.api.searchdirections.OdsaySearchDirectionApiRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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

    public OdsaySearchDirectionApiRoot odsayPublicTransportdirection(String sX, String sY, String eX, String eY) {
        URI uri = UriComponentsBuilder.fromUriString(odsayPublicTransportDirection)
                .queryParam("apiKey", odsayAPISecrectKey)
                .queryParam("lang", 0)
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




}
