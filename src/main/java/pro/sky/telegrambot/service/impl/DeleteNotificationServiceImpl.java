package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.repository.NotificationRepository;
import pro.sky.telegrambot.service.FindNotificationsService;
import pro.sky.telegrambot.service.SendBotMessageService;
import pro.sky.telegrambot.service.cash.BotState;
import pro.sky.telegrambot.service.cash.BotStateCash;
import pro.sky.telegrambot.util.ParsingUtil;

import java.util.Map;

import static pro.sky.telegrambot.service.cash.BotState.DELETE_FILL_ID_STATE;
import static pro.sky.telegrambot.service.cash.BotState.MAIN_MENU_STATE;
import static pro.sky.telegrambot.util.NotificationConst.*;
import static pro.sky.telegrambot.util.NotificationUtils.getChatId;
import static pro.sky.telegrambot.util.NotificationUtils.getMessage;
@Service
@RequiredArgsConstructor
public class DeleteNotificationServiceImpl implements DeleteNotificationService {
    private final NotificationRepository repository;
    private final BotStateCash botStateCash;
    private final SendBotMessageService messageService;
    private final FindNotificationsService findService;
    @Override
    @Transactional
    public void deleteNotification(Update update) {
        Long chatId = getChatId(update);
        Map<Long, BotState> botCash = botStateCash.getBotStateMap();
        BotState botState = botCash.get(chatId);
        switch (botState) {
            case MAIN_MENU_STATE: {
                messageService.sendMessage(chatId, ENTER_ID);
                botCash.put(chatId, DELETE_FILL_ID_STATE);
                break;
            }
            case DELETE_FILL_ID_STATE: {
                Long number = ParsingUtil.parseNumber(getMessage(update));

                if (number != null && findService.findNotificationByNumberAndUser_ChatId(
                        number, chatId).isPresent()) {
                    try {
                        repository.deleteNotificationById(number);
                        messageService.sendMessage(chatId, DELETE_OK);
                        botCash.put(chatId, MAIN_MENU_STATE);
                    } catch (Exception e) {
                        messageService.sendMessage(chatId, DELETE_FAIL + e.getMessage());
                    }
                } else {
                    messageService.sendMessage(chatId, WRONG_ID);
                }
                break;
            }


        }
    }


}

