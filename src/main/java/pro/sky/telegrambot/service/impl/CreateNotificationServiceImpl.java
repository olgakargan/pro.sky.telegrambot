package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.entity.Notification;
import pro.sky.telegrambot.entity.TelegramUser;
import pro.sky.telegrambot.repository.NotificationRepository;
import pro.sky.telegrambot.service.SendBotMessageService;
import pro.sky.telegrambot.service.cash.BotState;
import pro.sky.telegrambot.service.cash.BotStateCash;
import pro.sky.telegrambot.service.cash.NotificationCash;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import static pro.sky.telegrambot.service.cash.BotState.*;
import static pro.sky.telegrambot.util.NotificationConst.*;
import static pro.sky.telegrambot.util.NotificationUtils.getChatId;
import static pro.sky.telegrambot.util.NotificationUtils.getMessage;
import static pro.sky.telegrambot.util.ParsingUtil.parseDate;
import static pro.sky.telegrambot.util.ParsingUtil.parseTime;
@Service
@RequiredArgsConstructor
public class CreateNotificationServiceImpl implements CreateNotificationService {
    private final NotificationRepository repository;
    private final BotStateCash botStateCash;
    private final NotificationCash notificationCash;
    private final UserService userService;
    private final SendBotMessageService messageService;
    @Override
    @Transactional
    public void createNotification(Update update) {
        Long chatId = getChatId(update);
        Map<Long, Notification> noteCash = notificationCash.getNotificationMap();
        Map<Long, BotState> botCash = botStateCash.getBotStateMap();
        Notification notification =
                noteCash.getOrDefault(chatId, new Notification());
        BotState botState = botCash.get(chatId);
        String request = getMessage(update);
        switch (botState) {
            case MAIN_MENU_STATE: {
                messageService.sendMessage(chatId, ENTER_DATE);
                botCash.put(chatId, CREATE_FILL_DATE_STATE);
                break;
            }
            case CREATE_FILL_DATE_STATE: {
                LocalDate localDate = parseDate(request);
                if (localDate == null) {
                    messageService.sendMessage(chatId, WRONG_DATE);
                } else {
                    LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
                    notification.setTime(localDateTime);
                    botCash.put(chatId, CREATE_FILL_TIME_STATE);
                    messageService.sendMessage(chatId, ENTER_TIME);
                }
                break;
            }
            case CREATE_FILL_TIME_STATE: {
                LocalTime localTime = parseTime(request);
                if (localTime == null) {
                    messageService.sendMessage(chatId, WRONG_TIME);
                } else {
                    LocalDateTime localDateTime =
                            LocalDateTime.of(notification.getTime().toLocalDate(), localTime);
                    notification.setTime(localDateTime);
                    botCash.put(chatId, CREATE_FILL_TEXT_STATE);
                    messageService.sendMessage(chatId, ENTER_TEXT);
                }
                break;
            }
            case CREATE_FILL_TEXT_STATE: {
                TelegramUser user = userService.getUser(update);
                notification.setUser(user);
                notification.setText(request);
                try {
                    repository.save(notification);
                    botCash.put(chatId, MAIN_MENU_STATE);
                    messageService.sendMessage(chatId, SAVED_OK);
                    noteCash.remove(chatId);
                    notification = null;
                } catch (Exception e) {
                    messageService.sendMessage(chatId, SAVED_FAIL + e.getMessage());
                }
                break;
            }
        }
        if (notification != null) {
            noteCash.put(chatId, notification);
        }
    }


}