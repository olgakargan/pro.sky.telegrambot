package pro.sky.telegrambot.service.impl;

import pro.sky.telegrambot.entity.Notification;

import java.util.List;

public interface SchedulingService {

    List<Notification> getCurrentNotifications();

    void sendScheduledMessages();
}