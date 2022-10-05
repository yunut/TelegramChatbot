package com.jysdev.chatbot.handler;

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
    private PublicTransportHandler publicTransportHandler;

    @Autowired
    SeoulSubwayInfoHandler seoulSubwayInfoHandler;

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
        } else if(message.startsWith("/지하철")) {
            // 검색한 지하철 역의 열차 남은 시간과 목적지
            String stationName = message.replace("/지하철", "").trim();

            if(stationName.endsWith("역")) {
                stationName = stationName.substring(0,stationName.length()-1);
            }

            String seoulSubwayInfoMessage = seoulSubwayInfoHandler.seoulSubwayArriveInfo(stationName);

            sendMessage.enableMarkdown(true);
            sendMessage.setParseMode("MarkdownV2");
            sendMessage.setText(seoulSubwayInfoMessage);
        };

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
