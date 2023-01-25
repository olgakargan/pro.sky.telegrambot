package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Update;
import pro.sky.telegrambot.entity.Notification;

import java.util.Optional;
public interface FindNotificationsService {
    void findNotifications(Update update);
    Optional<Notification> findNotificationByNumberAndUser_ChatId(Long id, Long chatId);
    String findNotificationsByPart(Update update);
    String findAllNotifications(Update update);
}