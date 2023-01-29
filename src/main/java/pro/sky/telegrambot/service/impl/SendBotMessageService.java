package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.telegrambot.entity.Notification;

import java.util.List;

public interface SendBotMessageService {
    void sendMessage(Long chatId, String message);

    void sendMessageWithKeyboard(Long chatId, String message, boolean scheduling);

    void showKeyboard(SendMessage sendMessage);

    void sendInlineKeyboard(Long chatId);

    void sendMessages(List<Notification> notifications);

}