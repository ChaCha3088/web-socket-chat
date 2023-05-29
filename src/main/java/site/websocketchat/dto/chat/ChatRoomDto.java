package site.websocketchat.dto.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ChatRoomDto {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Long maxCapacity;

    @NotNull
    private Long currentCapacity;

    @Builder
    protected ChatRoomDto(Long id, String name, Long maxCapacity, Long currentCapacity) {
        this.id = id;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
    }
}
