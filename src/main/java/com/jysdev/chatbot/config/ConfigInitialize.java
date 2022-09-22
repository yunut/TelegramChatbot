package com.jysdev.chatbot.config;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigInitialize {

    private Dotenv dotenv;

    public void init() {

        // env loading
        dotenv = Dotenv.configure()
                .filename("env")
                .load();
    }

    public String getEnv(String envName) {
        return dotenv.get(envName);
    }
}
