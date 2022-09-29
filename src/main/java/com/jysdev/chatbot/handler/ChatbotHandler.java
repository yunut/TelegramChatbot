package com.jysdev.chatbot.handler;

import com.jysdev.chatbot.dto.gyeonggibusapi.searchStation.BusArriveInfo;
import com.jysdev.chatbot.dto.gyeonggibusapi.searchStation.BusStationInfo;
import com.jysdev.chatbot.dto.gyeonggibusapi.searchStation.BusStationRouteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class ChatbotHandler extends TelegramLongPollingBot {

    private final String userName;
    private final String token;

    @Autowired
    private GyeongGiBusStationHandler gyeongGiBusStationHandler;

    @Autowired
    private PublicTransportHandler publicTransportHandler;

    public ChatbotHandler(@Value("${telegram.chatbot.userName}") String userName, @Value("${telegram.chatbot.token}") String token) {
        this.userName = userName;
        this.token = token;
    }
    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {

        //TODO 명령어 입력에 따른 api 로직 분기

 /*       String message = update.getMessage().getText();
        String[] messageSplit = message.split(" ");
        busApiProcess(messageSplit[0],messageSplit[1]);*/
        String message = update.getMessage().getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        if(message.startsWith("/길찾기")) {
            // 출발지에서 목적지 길찾기 프로세스
            String[] point = message.replace("/길찾기", "").split(" ");
            String start = point[1].trim();
            String end = point[2].trim();
            String publicStationDirectionMessage = publicTransportHandler.publicStationDirection(start, end);

            sendMessage.enableMarkdown(true);
            sendMessage.setParseMode("MarkdownV2");
            sendMessage.setText(publicStationDirectionMessage);
            System.out.println(publicStationDirectionMessage);
        } else if(update.getMessage().toString().startsWith("/지하철")) {
            // 검색한 지하철 역의 열차 남은 시간과 목적지

        };

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }


    /*
    public void busApiProcess(String startStationName, String arriveStationName) {
        // 버스 정류장 검색
        BusStationInfo startStationInfo = gyeongGiBusStationHandler.busStationSearch(startStationName);
        BusStationInfo arriveStationInfo = gyeongGiBusStationHandler.busStationSearch(arriveStationName);

        // 검색된 최상단 정류장의 노선 검색
        BusStationRouteInfo[] startBusStationRouteInfos = gyeongGiBusStationHandler.busRouteNumberSearch(startStationInfo.getStationId());
        BusStationRouteInfo[] arriveBusStationRouteInfos = gyeongGiBusStationHandler.busRouteNumberSearch(arriveStationInfo.getStationId());
        List<BusStationRouteInfo> targetBusNumberList = gyeongGiBusStationHandler.targetBusNumberList(startBusStationRouteInfos, arriveBusStationRouteInfos);

        // 해당 번호의 출발지 도착 시간 검색
        BusArriveInfo[] busArriveInfos = gyeongGiBusStationHandler.busArriveInfoSearch(startStationInfo.getStationId());
        for(BusArriveInfo busArriveInfo : busArriveInfos) {
            // 차량번호, 첫번쨰 차량 도착정보, 두번쨰 차량 도착정보 (출발 정류소 이름, 도착 정류소 이름) 출력
        }

        // 챗봇 출력 데이터 가공
    }
     */
}
