package demo.chat.moderator.repo;

import demo.chat.moderator.model.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageEntityRepository
        extends JpaRepository<ChatMessageEntity, String> {
    List<ChatMessageEntity> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
