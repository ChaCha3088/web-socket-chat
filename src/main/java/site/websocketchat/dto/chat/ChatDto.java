package site.websocketchat.dto.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.websocketchat.enumstorage.chat.MessageType;

@Getter
@NoArgsConstructor
public class ChatDto {
    private MessageType messageType;
    private Long chatRoomId;
    private Long memberId;
    private String writer;
    private String message;

    @Builder
    protected ChatDto(MessageType messageType, Long chatRoomId, Long memberId, String writer, String message) {
        this.messageType = messageType;
        this.chatRoomId = chatRoomId;
        this.memberId = memberId;
        this.writer = writer;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
