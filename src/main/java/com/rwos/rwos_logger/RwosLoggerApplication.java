package com.rwos.rwos_logger;

import com.rwos.rwos_logger.Bot.rwosLoggerBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class RwosLoggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RwosLoggerApplication.class, args);
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new rwosLoggerBot());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
