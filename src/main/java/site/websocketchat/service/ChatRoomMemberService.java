package site.websocketchat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.websocketchat.enumstorage.errormessage.ChatRoomErrorMessage;
import site.websocketchat.exception.ChatRoomException;
import site.websocketchat.repository.chat.ChatRoomMemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomMemberService {
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public void findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId) {
        //chatRoomMember에 똑같은 속성의 chatRoomMember가 있는지 확인
        chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId)
                //있으면 예외 발생
                .ifPresent(chatRoomMember -> {
                    throw new ChatRoomException(ChatRoomErrorMessage.ALREADY_JOINED.getMessage());
                });
    }
}
