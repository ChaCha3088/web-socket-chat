package site.websocketchat.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import site.websocketchat.domain.ChatRoomMember;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long>, ChatRoomMemberRepositoryQueryDsl {
    Optional<ChatRoomMember> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);
    Optional<ChatRoomMember> findWithChatRoomAndMemberBySessionId(String sessionId);
    List<ChatRoomMember> findByChatRoomId(Long chatRoomId);
}
