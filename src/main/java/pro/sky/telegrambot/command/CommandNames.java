package pro.sky.telegrambot.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CommandNames {
    GREET_COMMAND("/start"),
    START_COMMAND("Запустить напоминания"),
    STOP_COMMAND("Остановить напоминания"),
    HELP_COMMAND("Помощь"),
    UNKNOWN_COMMAND("Неизвестная команда"),
    CREATE_COMMAND("Создать"),
    EDIT_COMMAND("Изменить"),
    FIND_COMMAND("Найти"),
    DELETE_COMMAND("Удалить"),
    CANCEL_COMMAND("Отменить"),
    ALL_COMMAND("Найти все");

    @Getter
    private final String commandName;

    public static CommandNames valueOfCommandName(String name) {
        for (CommandNames value : values()) {
            if (value.commandName.equals(name)) {
                return value;
            }
        }
        return CommandNames.UNKNOWN_COMMAND;
    }

}