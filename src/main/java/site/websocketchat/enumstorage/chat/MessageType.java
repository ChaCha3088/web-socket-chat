package site.websocketchat.enumstorage.chat;

import lombok.Getter;

@Getter
public enum MessageType {
    ENTER, TALK, LEAVE;

    MessageType() {
    }
}