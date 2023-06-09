package site.websocketchat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import site.websocketchat.domain.ChatRoomMember;
import site.websocketchat.dto.chat.ChatDto;
import site.websocketchat.enumstorage.chat.MessageType;
import site.websocketchat.service.ChatRoomService;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatRoomService chatRoomService;

    //Config Class의 registry.setApplicationDestinationPrefixes("/pub")으로 설정한 접두어 "/pub"가 앞에 붙는다.
    @MessageMapping("/chat-room/join")
    public void joinChatRoom(@Payload ChatDto chatDto, SimpMessageHeaderAccessor headerAccessor) {
        //채팅방에 입장하여 클라이언트가 메시지를 보내면, 이 부분에서 sessionId를 얻을 수 있다.
        String sessionId = headerAccessor.getSessionId();

        chatRoomService.joinChatRoom(chatDto.getChatRoomId(), chatDto.getMemberId(), sessionId);

        chatDto.setMessage(chatDto.getWriter() + "님이 입장하셨습니다.");
        simpMessageSendingOperations.convertAndSend("/sub/chat-room/" + chatDto.getChatRoomId(), chatDto);
    }

    @MessageMapping("/chat-room/send-message")
    public void sendMessage(@Payload ChatDto chatDto) {
        simpMessageSendingOperations.convertAndSend("/sub/chat-room/" + chatDto.getChatRoomId(), chatDto);
    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();

        ChatRoomMember chatRoomMember = chatRoomService.leaveChatRoom(sessionId);

        ChatDto chatDto = ChatDto.builder()
                .messageType(MessageType.LEAVE)
                .chatRoomId(chatRoomMember.getChatRoom().getId())
                .memberId(chatRoomMember.getMember().getId())
                .writer(chatRoomMember.getMember().getEmail())
                .message(chatRoomMember.getMember().getEmail() + "님이 퇴장하셨습니다.")
                .build();

        simpMessageSendingOperations.convertAndSend("/sub/chat-room/" + chatRoomMember.getChatRoom().getId(), chatDto);
    }
}
