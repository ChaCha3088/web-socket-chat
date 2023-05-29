package site.websocketchat.repository.chat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import site.websocketchat.domain.ChatRoomMember;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static site.websocketchat.domain.QChatRoomMember.chatRoomMember;

@RequiredArgsConstructor
public class ChatRoomMemberRepositoryImpl implements ChatRoomMemberRepositoryQueryDsl {
    private final EntityManager em;

    @Override
    public Optional<ChatRoomMember> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        return Optional.ofNullable(queryFactory
                        .selectFrom(chatRoomMember)
                        .where(chatRoomMember.chatRoom.id.eq(chatRoomId)
                                .and(chatRoomMember.member.id.eq(memberId)))
                        .fetchOne());
    }

    @Override
    public Optional<ChatRoomMember> findWithChatRoomAndMemberBySessionId(String sessionId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        return Optional.ofNullable(queryFactory
                        .selectFrom(chatRoomMember)
                        .where(chatRoomMember.sessionId.eq(sessionId))
                        .join(chatRoomMember.chatRoom).fetchJoin()
                        .join(chatRoomMember.member).fetchJoin()
                .fetchOne());
    }

    @Override
    public List<ChatRoomMember> findByChatRoomId(Long chatRoomId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        return queryFactory
                .selectFrom(chatRoomMember)
                .where(chatRoomMember.chatRoom.id.eq(chatRoomId))
                .fetch();
    }

}
