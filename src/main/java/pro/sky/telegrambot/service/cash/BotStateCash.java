package pro.sky.telegrambot.service.cash;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class BotStateCash {
    private final Map<Long, BotState> botStateMap = new HashMap<>();

}