package listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.command.CommandsBox;
import pro.sky.telegrambot.service.cash.BotState;
import pro.sky.telegrambot.service.cash.BotStateCash;

import javax.annotation.PostConstruct;
import java.util.List;

import static pro.sky.telegrambot.command.CommandConst.COMMAND_SUFFIX;
import static pro.sky.telegrambot.command.CommandNames.CANCEL_COMMAND;
import static pro.sky.telegrambot.command.CommandNames.valueOf;
import static pro.sky.telegrambot.service.cash.BotState.MAIN_MENU_STATE;
import static pro.sky.telegrambot.util.NotificationUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final CommandsBox commandsBox;


    private final BotStateCash botStateCash;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }


    @Override
    public int process(List<Update> updates) {
        BotState botState;

        for (Update update : updates) {
            //todo use updateDto
            log.info("Processing update: {}", update);
            String message = getMessage(update);
            if (message == null) {
                continue;
            }
            Long chatId = getChatId(update);

            if (!botStateCash.getBotStateMap().containsKey(chatId) ||
                    getMessage(update).equals(CANCEL_COMMAND.getCommandName())) {
                botStateCash.getBotStateMap().put(chatId, MAIN_MENU_STATE);
            }
            botState = botStateCash.getBotStateMap().get(chatId);

            if (botState.equals(MAIN_MENU_STATE)) {
                commandsBox.findCommand(message).execute(update);
            } else {
                String beginBotSt = botStatePrefix(botState);
                commandsBox.findCommand(
                                valueOf(beginBotSt + COMMAND_SUFFIX).getCommandName())
                        .execute(update);
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}