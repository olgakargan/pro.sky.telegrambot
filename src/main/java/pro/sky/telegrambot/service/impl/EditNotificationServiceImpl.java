package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.entity.Notification;
import pro.sky.telegrambot.repository.NotificationRepository;
import pro.sky.telegrambot.service.FindNotificationsService;
import pro.sky.telegrambot.service.SendBotMessageService;
import pro.sky.telegrambot.service.cash.BotState;
import pro.sky.telegrambot.service.cash.BotStateCash;
import pro.sky.telegrambot.service.cash.NotificationCash;
import pro.sky.telegrambot.util.ParsingUtil;

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
public class EditNotificationServiceImpl implements EditNotificationService {
    private final NotificationRepository repository;
    private final BotStateCash botStateCash;
    private final NotificationCash notificationCash;
    private final SendBotMessageService messageService;
    private final FindNotificationsService findService;

    @Override
    @Transactional
    public void editNotification(Update update) {
        Long chatId = getChatId(update);
        Map<Long, Notification> noteCash = notificationCash.getNotificationMap();
        Map<Long, BotState> botCash = botStateCash.getBotStateMap();
        Notification notificationOld;
        Notification notificationNew =
                noteCash.getOrDefault(chatId, new Notification());
        BotState botState = botCash.get(chatId);
        String request = getMessage(update);

        switch (botState) {
            case MAIN_MENU_STATE: {
                messageService.sendMessage(chatId, ENTER_ID);
                botCash.put(chatId, EDIT_FILL_ID_STATE);
                break;
            }

            case EDIT_FILL_ID_STATE: {
                Long id = ParsingUtil.parseNumber(request);

                if (id != null && findService.findNotificationByNumberAndUser_ChatId(
                        id, chatId).isPresent()) {
                    notificationNew.setId(id);
                    messageService.sendMessage(chatId, ENTER_DATE);
                    botCash.put(chatId, EDIT_FILL_DATE_STATE);
                } else {
                    messageService.sendMessage(chatId, WRONG_ID);
                }
                break;
            }

            case EDIT_FILL_DATE_STATE: {
                LocalDate localDate = parseDate(request);
                if (localDate == null) {
                    messageService.sendMessage(chatId, WRONG_DATE);
                } else {
                    LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
                    notificationNew.setTime(localDateTime);
                    botCash.put(chatId, EDIT_FILL_TIME_STATE);
                    messageService.sendMessage(chatId, ENTER_TIME);
                }
                break;
            }

            case EDIT_FILL_TIME_STATE: {
                LocalTime localTime = parseTime(request);
                if (localTime == null) {
                    messageService.sendMessage(chatId, WRONG_TIME);
                } else {
                    LocalDateTime localDateTime =
                            LocalDateTime.of(notificationNew.getTime().toLocalDate(), localTime);
                    notificationNew.setTime(localDateTime);
                    botCash.put(chatId, EDIT_FILL_TEXT_STATE);
                    messageService.sendMessage(chatId, ENTER_TEXT);
                }
                break;
            }

            case EDIT_FILL_TEXT_STATE: {
                notificationOld = findService.findNotificationByNumberAndUser_ChatId(
                        notificationNew.getId(), chatId).get();
                notificationOld.setTime(notificationNew.getTime());
                notificationOld.setText(request);

                try {
                    repository.save(notificationOld);
                    botCash.put(chatId, MAIN_MENU_STATE);
                    messageService.sendMessage(chatId, EDIT_OK);
                } catch (Exception e) {
                    messageService.sendMessage(chatId, EDIT_FAIL + e.getMessage());
                }
                break;
            }
        }
        noteCash.put(chatId, notificationNew);
    }
    //todo при нажатии на "ввод", т.е введении пустой строки, данные, которые не
    // предполагается изменять, сохранялись бы прежними При нажатии на кнопку Изменить появляется
    // клавиатура, в которой на каждой кнопке - текст сохраненного напоминания. При нажатии на
    // такую кнопку появляются кнопки Дата, Время, Текст, Сохранить. После нажатия на первые три
    // следует вводить соответствуещее новое значение, далее следует сохранить нажатием на Сохранить
}
