package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Notification;
import pro.sky.telegrambot.service.SendBotMessageService;
import java.util.List;
import static org.apache.logging.log4j.util.Strings.isBlank;
import static pro.sky.telegrambot.command.CommandNames.*;
@Service
@RequiredArgsConstructor
public class SendBotMessageServiceImpl implements SendBotMessageService {
    private final TelegramBot telegramBot;
    private final String[][] buttons = {{STOP_COMMAND.getCommandName(), HELP_COMMAND.getCommandName()},
            {CREATE_COMMAND.getCommandName(), EDIT_COMMAND.getCommandName(), DELETE_COMMAND.getCommandName()},
            {FIND_COMMAND.getCommandName(), ALL_COMMAND.getCommandName(), CANCEL_COMMAND.getCommandName()}};
    @Override
    public void sendMessage(Long chatId, String message) {
        if (isBlank(message)) return;
        SendMessage sendMessage = new SendMessage(chatId, message).parseMode(ParseMode.HTML);
        telegramBot.execute(sendMessage);
    }

    @Override
    public void sendMessageWithKeyboard(Long chatId, String message, boolean scheduling) {
        if (isBlank(message)) return;
        SendMessage sendMessage = new SendMessage(chatId, message);
        if (scheduling) {
            buttons[0][0] = STOP_COMMAND.getCommandName();
        } else {
            buttons[0][0] = START_COMMAND.getCommandName();
        }
        showKeyboard(sendMessage);
    }

    @Override
    public void showKeyboard(SendMessage sendMessage) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(buttons);
        keyboardMarkup.resizeKeyboard(true);
        sendMessage.replyMarkup(keyboardMarkup);
        telegramBot.execute(sendMessage);
    }
    @Override
    public void sendInlineKeyboard(Long chatId) {
        SendMessage message = new SendMessage(chatId, "Inline model below.");
        InlineKeyboardButton youtube =
                new InlineKeyboardButton("youtube").url("https://www.youtube.com");
        InlineKeyboardButton github =
                new InlineKeyboardButton("github").url("https://github.com");
        InlineKeyboardButton[] inlineButtons = {youtube, github};
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(inlineButtons);
        message.replyMarkup(inlineKeyboardMarkup);
        try {
            telegramBot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void sendMessages(List<Notification> notifications) {
        notifications.forEach(notification -> {
            Long chatId = notification.getUser().getChatId();
            String message = notification.getText();
            SendMessage sendMessage = new SendMessage(chatId, message).parseMode(ParseMode.HTML);
            telegramBot.execute(sendMessage);
        });
    }
}

