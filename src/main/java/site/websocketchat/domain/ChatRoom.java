package site.websocketchat.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.websocketchat.dto.chat.ChatRoomDto;
import site.websocketchat.enumstorage.errormessage.ChatRoomErrorMessage;
import site.websocketchat.exception.ChatRoomException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @NotNull
    private Long maxCapacity;

    @NotNull
    private Long currentCapacity = 0L;

    @NotNull
    @OneToMany(mappedBy = "chatRoom")
    private Set<ChatRoomMember> chatRoomMembers = new HashSet<>();

    @Builder
    protected ChatRoom(String name, Long maxCapacity) {
        this.name = name;
        this.maxCapacity = maxCapacity;
    }

    //== 비즈니스 로직 ==//
    public void joinChatRoomMember(ChatRoomMember chatRoomMember) {
        if (this.getCurrentCapacity() >= this.getMaxCapacity()) {
            throw new ChatRoomException(ChatRoomErrorMessage.CHAT_ROOM_IS_FULL.getMessage());
        }

        chatRoomMembers.add(chatRoomMember);
        currentCapacity += 1;
    }

    public void leaveChatRoomMember(ChatRoomMember chatRoomMember) {
        chatRoomMembers.remove(chatRoomMember);
        currentCapacity -= 1;
    }

    //== Dto ==//
    public ChatRoomDto toChatRoomDto() {
        return ChatRoomDto.builder()
                .id(this.getId())
                .name(this.getName())
                .maxCapacity(this.getMaxCapacity())
                .currentCapacity(this.getCurrentCapacity())
                .build();
    }
}
