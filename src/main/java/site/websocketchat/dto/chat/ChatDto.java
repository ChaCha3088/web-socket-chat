package site.websocketchat.dto.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatDto {
    private String type;
    private Long chatRoomId;
    private Long memberId;
    private String writer;
    private String message;

    @Builder
    protected ChatDto(String type, Long chatRoomId, Long memberId, String writer, String message) {
        this.type = type;
        this.chatRoomId = chatRoomId;
        this.memberId = memberId;
        this.writer = writer;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
