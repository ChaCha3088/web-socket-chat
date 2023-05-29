package site.websocketchat.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.websocketchat.domain.member.Member;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMember {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String sessionId;

    @ManyToOne(fetch = LAZY)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = LAZY)
    private Member member;

    @Builder
    protected ChatRoomMember(String sessionId, ChatRoom chatRoom, Member member) {
        this.sessionId = sessionId;
        this.chatRoom = chatRoom;
        this.member = member;

        chatRoom.joinChatRoomMember(this);
        member.joinChatRoomMember(this);
    }
}
