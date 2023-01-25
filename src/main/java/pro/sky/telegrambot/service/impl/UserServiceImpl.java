package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.TelegramUser;
import pro.sky.telegrambot.repository.UserRepository;

import static pro.sky.telegrambot.util.NotificationUtils.getChatId;
import static pro.sky.telegrambot.util.NotificationUtils.getUserName;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    @Override
    public TelegramUser create(Long chatId, String name) {
        TelegramUser user = new TelegramUser();
        user.setName(name);
        user.setChatId(chatId);
        return repository.save(user);
    }
    @Override
    public void updateUserName(Update update) {
        TelegramUser user = repository.getTelegramUserByChatId(getChatId(update));
        if (user != null) {
            user.setName(getUserName(update));
            repository.save(user);
        }
    }
    @Override
    public TelegramUser getUser(Update update) {
        TelegramUser user = repository.getTelegramUserByChatId(getChatId(update));
        if (user == null) {
            user = create(getChatId(update), getUserName(update));
        }
        return user;
    }
}
