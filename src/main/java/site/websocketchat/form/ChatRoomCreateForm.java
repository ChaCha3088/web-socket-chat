package site.websocketchat.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomCreateForm {
    private String name;
    private Long maxCapacity;
}
