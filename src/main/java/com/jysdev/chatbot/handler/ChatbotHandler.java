package com.jysdev.chatbot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

        // 지원하지 않는 명령어가 들어왔을 시 도움말 처리
        if(sendMessage.getText() == null) {
            String infoMessage = "";
            infoMessage += String.format("ㅇ 현재 지원 명령어\n\n");
            infoMessage += String.format("*/길찾기* \\- 출발지에서 목적지까지 대중교통 최적 경로를 탐색합니다\\.\n");
            infoMessage += String.format("  예\\) /길찾기 부천시청 여의도물빛공원\n\n");
            infoMessage += String.format("*/지하철* \\- 지정한 역의 지하철 도착 예정시간을 알려줍니다\\.\n");
            infoMessage += String.format("  예\\) /지하철 부천시청역\n");

            sendMessage.enableMarkdown(true);
            sendMessage.setParseMode("MarkdownV2");
            sendMessage.setText(infoMessage);
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
