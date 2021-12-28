package com.home.cameratomjpeg.config;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

import static com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL;

@Slf4j
@Configuration
public class TelegramBotConfig {

    @Bean
    public TelegramBot telegramBot(ApplicationConfigEntity applicationConfig) {
        String token = applicationConfig.getTelegramBotToken();
        if (token == null || token.isEmpty()) {
            log.warn("Telegram bot token not provided");
            return null;
        }

        TelegramBot telegramBot = new TelegramBot(token);
        telegramBot.setUpdatesListener(updates -> {
            updates.stream()
                    .filter(Objects::nonNull)
                    .map(Update::message)
                    .map(Message::chat)
                    .map(Chat::id)
                    .findFirst()
                    .ifPresent(chatId -> log.info("Receive update from chat with id: {}", chatId));

            return CONFIRMED_UPDATES_ALL;
        });
        return telegramBot;
    }
}
