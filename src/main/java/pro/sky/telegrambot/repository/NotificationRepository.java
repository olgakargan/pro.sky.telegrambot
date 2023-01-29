package pro.sky.telegrambot.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.telegrambot.entity.Notification;
import java.util.List;
import java.util.Optional;
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findNotificationByUser_ChatId(Long chatId, Sort time);
    @Query(value = "select n from Notification n join fetch n.user " +
            "where n.user.chatId = :chatId and lower(n.text) like %:word%")
    List<Notification> findNotificationsByPart(@Param("chatId") Long chatId, @Param("word") String word);
    @Query(value = "select * from notifications " +
            "where date_trunc('minute', time) = date_trunc('minute', now())",
            nativeQuery = true)
    List<Notification> getCurrentNotifications();
    Optional<Notification> findNotificationByIdAndUser_ChatId(Long id, Long chatId);
    void deleteNotificationById(Long Id);
    // not working
    @Query(value = "select n from Notification n join TelegramUser u " +
            "on n.user.chatId=u.chatId " +
            "where upper(n.text)  like %:word% and n.user.chatId = :chatId")
    List<Notification> findNotificationsByWord(
            @Param("chatId") Long chatId, @Param("word") String word);
    @Query(value = "   select\n" +
            "        n.id as id1_0_,\n" +
            "        n.text as text2_0_,\n" +
            "        n.time as time3_0_,\n" +
            "        n.user_id as user_id4_0_ \n" +
            "    from\n" +
            " notifications n \n" +
            "    left outer join\n" +
            "        users telegramus1_ \n" +
            "            on n.user_id=telegramus1_.id \n" +
            "    where\n" +
            "        telegramus1_.chat_id=?1 \n" +
            "        and (\n" +
            "            n.text ilike '%'||?2||'%'\n" +
            "        )", nativeQuery = true
    )
    List<Notification> findNotificationsByPart1(
            @Param("chatId") Long chatId, @Param("word") String word);
    @Query(value = "select n from Notification n join fetch n.user " +
            "where n.user.chatId = :chatId")
    List<Notification> findNotifications(Long chatId, Sort time);
    List<Notification> findNotificationByUser_ChatIdAndTextIgnoreCaseContaining(Long chatId, String word);
    List<Notification> findNotificationsByUser_ChatIdAndTextContainingIgnoreCase(Long chatId, String word);
}
