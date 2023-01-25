package pro.sky.telegrambot.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import static pro.sky.telegrambot.util.ParsingUtil.formatTime;
@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = {"time", "user_id"})
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime time;
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private TelegramUser user;
    @Override
    public String toString() {
        return String.format("<b>Напоминание №%d</b> %s %s", id, formatTime(time), text);
    }


}