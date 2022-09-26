package com.jysdev.chatbot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ChatbotHandler extends TelegramLongPollingBot {

    private final String userName;
    private final String token;

    @Autowired
    private GyeongGiBusStationHandler gyeongGiBusStationHandler;

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
        gyeongGiBusStationHandler.busStationSearch(update.getMessage().getText());

    }
}
