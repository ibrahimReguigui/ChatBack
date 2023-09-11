package com.example.wschatapp.repo;

import com.example.wschatapp.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAll();

    List<Chat> findAllBySenderOrRecipient(String user1, String user2);

    @Query(value = "SELECT * FROM Chat_Message c WHERE c.sender like ?1 AND c.recipient like ?2"
            , nativeQuery = true)
    List<Chat> findConversation(String user1, String user2);

    @Modifying
    @Query("delete from Chat c where c.sender=:sender and c.recipient=:recipient and c.content=:content and " +
            "c.time=:time")
    void deleteMsg(@Param("sender") String sender, @Param("recipient") String recipient,
                   @Param("content") String content, @Param("time") LocalDateTime time);

    List<Chat> findChatBySenderAndRecipientAndContent(
            String sender,String recipient,String content);

}
