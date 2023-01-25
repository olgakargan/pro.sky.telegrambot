package pro.sky.telegrambot.command;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.service.FindNotificationsService;
import pro.sky.telegrambot.service.SendBotMessageService;
import pro.sky.telegrambot.service.impl.*;

import javax.annotation.PostConstruct;
import java.util.EnumMap;

import static pro.sky.telegrambot.command.CommandConst.*;
import static pro.sky.telegrambot.command.CommandNames.*;
import static pro.sky.telegrambot.util.NotificationUtils.getChatId;
import static pro.sky.telegrambot.util.NotificationUtils.getUserName;

@Component
    @RequiredArgsConstructor
    public class CommandsBox {
        private final EnumMap<CommandNames, Command> commands = new EnumMap<>(CommandNames.class);
        private final SendBotMessageService sendService;
        private final CreateNotificationService createService;
        private final EditNotificationService editService;
        private final FindNotificationsService findService;
        private final DeleteNotificationService deleteService;
        private final UserService userService;
        private final SchedulingServiceImpl schedulingService;
        @PostConstruct
        private void initCommands() {
            schedulingService.setScheduling(true);
            sendService.showKeyboard(new SendMessage("", GREETINGS_MESSAGE));
            // first row of buttons
            commands.put(GREET_COMMAND, update -> {
                // если имя пользователя вдруг изменилось, обновляю его в базе
                userService.updateUserName(update);
                String greetings = String.format("%s %s !", GREETINGS_MESSAGE, getUserName(update));
                sendService.sendMessageWithKeyboard(getChatId(update), greetings, true);
            });

            commands.put(START_COMMAND, update -> {
                schedulingService.setScheduling(true);
                sendService.sendMessageWithKeyboard(getChatId(update),
                        schedulingService.getEnabled(), true);
            });

            commands.put(STOP_COMMAND, update -> {
                schedulingService.setScheduling(false);
                sendService.sendMessageWithKeyboard(getChatId(update),
                        schedulingService.getEnabled(), false);
            });
            commands.put(HELP_COMMAND, update -> sendService.sendMessage(getChatId(update), HELP_MESSAGE));
            // second row of buttons
            commands.put(CREATE_COMMAND, createService::createNotification);
            commands.put(EDIT_COMMAND, editService::editNotification);
            commands.put(DELETE_COMMAND, deleteService::deleteNotification);
            // third row of buttons
            commands.put(FIND_COMMAND, findService::findNotifications);
            commands.put(ALL_COMMAND, update -> sendService.sendMessage(getChatId(update),
                    findService.findAllNotifications(update)));
            commands.put(CANCEL_COMMAND, update ->
                    sendService.sendMessage(getChatId(update), CANCEL));
            //
            commands.put(UNKNOWN_COMMAND, update -> sendService.sendMessage(getChatId(update), UNKNOWN_MESSAGE));
        }
        public Command findCommand(String commandName) {
            return commands.getOrDefault(valueOfCommandName(commandName),
                    commands.get(UNKNOWN_COMMAND));
        }


}
