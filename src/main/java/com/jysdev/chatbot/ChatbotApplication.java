package com.jysdev.chatbot;

import com.jysdev.chatbot.config.ConfigInitialize;
import com.jysdev.chatbot.handler.ChatbotHandler;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class ChatbotApplication {

    public static void main(String[] args) {

        // config init
        ConfigInitialize configInitialize = new ConfigInitialize();
        configInitialize.init();

        // chatbot init
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new ChatbotHandler(configInitialize.getEnv("userName"), configInitialize.getEnv("token")));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }

}
