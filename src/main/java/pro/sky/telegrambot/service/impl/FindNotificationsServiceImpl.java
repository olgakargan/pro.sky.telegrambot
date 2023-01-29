package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Notification;
import pro.sky.telegrambot.repository.NotificationRepository;
import pro.sky.telegrambot.service.FindNotificationsService;
import pro.sky.telegrambot.service.SendBotMessageService;
import pro.sky.telegrambot.service.cash.BotState;
import pro.sky.telegrambot.service.cash.BotStateCash;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static pro.sky.telegrambot.service.cash.BotState.FIND_FILL_EXCERPT_STATE;
import static pro.sky.telegrambot.service.cash.BotState.MAIN_MENU_STATE;
import static pro.sky.telegrambot.util.NotificationConst.ENTER_PART;
import static pro.sky.telegrambot.util.NotificationUtils.*;
@Service
@RequiredArgsConstructor
public class FindNotificationsServiceImpl implements FindNotificationsService {
    private final NotificationRepository repository;
    private final BotStateCash botStateCash;
    private final SendBotMessageService messageService;

    @Override
    public void findNotifications(Update update) {
        Long chatId = getChatId(update);
        Map<Long, BotState> botCash = botStateCash.getBotStateMap();
        BotState botState = botCash.get(chatId);
        switch (botState) {
            case MAIN_MENU_STATE: {
                messageService.sendMessage(chatId, ENTER_PART);
                botCash.put(chatId, FIND_FILL_EXCERPT_STATE);
                break;
            }
            case FIND_FILL_EXCERPT_STATE: {
                String response = findNotificationsByPart(update);
                messageService.sendMessage(chatId, response);
                botCash.put(chatId, MAIN_MENU_STATE);
                break;
            }
        }
    }

    @Override
    public Optional<Notification> findNotificationByNumberAndUser_ChatId(Long id, Long chatId) {
        return repository.findNotificationByIdAndUser_ChatId(id, chatId);
    }

    @Override
    public String findNotificationsByPart(Update update) {
        List<Notification> list = repository
                .findNotificationsByPart(
                        getChatId(update), getMessage(update).toLowerCase());
        return listToString(list);
    }

    @Override
    public String findAllNotifications(Update update) {
        return listToString(repository.findNotificationByUser_ChatId(getChatId(update),
                Sort.by(Sort.Direction.ASC, "id")));
    }
}