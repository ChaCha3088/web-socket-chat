package site.websocketchat.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import site.websocketchat.domain.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryQueryDsl {
}
