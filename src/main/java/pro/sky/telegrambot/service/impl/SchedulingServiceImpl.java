package pro.sky.telegrambot.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Notification;
import pro.sky.telegrambot.repository.NotificationRepository;
import pro.sky.telegrambot.service.SendBotMessageService;

import java.util.List;

import static pro.sky.telegrambot.command.CommandConst.START_SCHEDULING;
import static pro.sky.telegrambot.command.CommandConst.STOP_SCHEDULING;

@Service
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService {
    public static final String cronString = "0 0/1 * * * *";
    private final NotificationRepository repository;
    private final SendBotMessageService messageService;
    @Getter
    @Setter
    private boolean scheduling;

    @Override
    public List<Notification> getCurrentNotifications() {
        return repository.getCurrentNotifications();
    }

    //todo Добавить возможность выбирать частоту напоминания
    // добавить возможность выбора часовых поясов
    // деплой на heroku
    @Scheduled(cron = cronString)
    @Override
    public void sendScheduledMessages() {
        if (!scheduling) {
            return;
        }
        messageService.sendMessages(getCurrentNotifications());
    }

    public String getEnabled() {
        return scheduling ? START_SCHEDULING : STOP_SCHEDULING;
    }
}