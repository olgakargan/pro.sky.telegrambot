package pro.sky.telegrambot.service.cash;

import lombok.Getter;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.entity.Notification;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class NotificationCash {
    private final Map<Long, Notification> notificationMap = new HashMap<>();
}