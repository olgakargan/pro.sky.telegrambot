package pro.sky.telegrambot.util;

import com.pengrad.telegrambot.model.Update;
import lombok.experimental.UtilityClass;
import pro.sky.telegrambot.entity.Notification;
import pro.sky.telegrambot.service.cash.BotState;

import java.util.List;
import java.util.stream.Collectors;

import static pro.sky.telegrambot.command.CommandConst.NEW_LINE;
import static pro.sky.telegrambot.util.NotificationConst.FOUND_NOTIFICATIONS;
import static pro.sky.telegrambot.util.NotificationConst.NOT_FOUND;

@UtilityClass
public class NotificationUtils {
    public static Long getChatId(Update update) {
        return update.message().chat().id();
    }

    public static String getMessage(Update update) {
        if (update == null || update.message() == null) {
            return null;
        }
        return update.message().text();
    }

    public static String getUserName(Update update) {
        return update.message().from().firstName();
    }

    public static String listToString(List<Notification> list) {
        if (list.isEmpty()) {
            return NOT_FOUND;
        }
        return FOUND_NOTIFICATIONS + "\n\n" + list.stream()
                .map(Notification::toString)
                .collect(Collectors.joining(NEW_LINE));
    }

    public static String botStatePrefix(BotState botState) {
        String name = botState.name();
        int index = name.indexOf("_");
        if (index > 0) {
            return name.substring(0, index);
        }
        return name;
    }


}