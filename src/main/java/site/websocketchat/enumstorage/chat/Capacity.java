package site.websocketchat.enumstorage.chat;

import lombok.Getter;

@Getter
public enum Capacity {
    MAX(100),
    MIN(2);

    private int capacity;

    Capacity(int capacity) {
        this.capacity = capacity;
    }
}
