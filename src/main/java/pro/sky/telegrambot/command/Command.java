package pro.sky.telegrambot.command;

import com.pengrad.telegrambot.model.Update;

public interface Command {
    void execute(Update update);
}