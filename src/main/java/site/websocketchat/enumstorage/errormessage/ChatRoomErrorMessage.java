package site.websocketchat.enumstorage.errormessage;

import lombok.Getter;

@Getter
public enum ChatRoomErrorMessage {
    CAPACITY_EXCEEDS_MAX("채팅방 최대 인원을 초과했습니다."),
    CAPACITY_BELOW_MIN("채팅방 최소 인원보다 적습니다."),
    NAME_IS_BLANK("채팅방 이름을 입력해주세요."),
    CHAT_ROOM_NOT_FOUND("채팅방을 찾을 수 없습니다."),
    CHAT_ROOM_IS_FULL("채팅방이 가득 찼습니다."),
    ALREADY_JOINED("이미 참여한 채팅방입니다."),
    NOT_JOINED("참여하지 않은 채팅방입니다.");

    private String message;

    ChatRoomErrorMessage(String message) {
        this.message = message;
    }
}
