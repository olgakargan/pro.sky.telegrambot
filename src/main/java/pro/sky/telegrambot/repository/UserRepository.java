package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.entity.TelegramUser;

public interface UserRepository extends JpaRepository<TelegramUser, Long> {
    TelegramUser getTelegramUserByChatId(Long chatId);

}