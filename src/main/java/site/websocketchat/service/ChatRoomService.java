package site.websocketchat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.websocketchat.domain.ChatRoom;
import site.websocketchat.domain.ChatRoomMember;
import site.websocketchat.domain.member.Member;
import site.websocketchat.dto.chat.ChatRoomDto;
import site.websocketchat.enumstorage.chat.Capacity;
import site.websocketchat.enumstorage.errormessage.ChatRoomErrorMessage;
import site.websocketchat.enumstorage.errormessage.MemberErrorMessage;
import site.websocketchat.exception.ChatRoomException;
import site.websocketchat.repository.chat.ChatRoomMemberRepository;
import site.websocketchat.repository.chat.ChatRoomRepository;
import site.websocketchat.repository.member.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public ChatRoomDto findById(Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new ChatRoomException(ChatRoomErrorMessage.CHAT_ROOM_NOT_FOUND.getMessage()))
                .toChatRoomDto();
    }

    public List<ChatRoomDto> findAllChatRooms() {
        return chatRoomRepository.findAll().stream()
                .map(ChatRoom::toChatRoomDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createChatRoom(String name, Long capacity) throws ChatRoomException {
        if (capacity > Capacity.MAX.getCapacity()) {
            throw new ChatRoomException(ChatRoomErrorMessage.CAPACITY_EXCEEDS_MAX.getMessage());
        }

        if (capacity < Capacity.MIN.getCapacity()) {
            throw new ChatRoomException(ChatRoomErrorMessage.CAPACITY_BELOW_MIN.getMessage());
        }

        if (name.isBlank()) {
            throw new ChatRoomException(ChatRoomErrorMessage.NAME_IS_BLANK.getMessage());
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .name(name)
                .maxCapacity(capacity)
                .build();

        return chatRoomRepository.save(chatRoom).getId();
    }

    @Transactional
    public void joinChatRoom(Long chatRoomId, Long memberId, String sessionId) {
        //chatRoomMember에 똑같은 속성의 chatRoomMember가 있는지 확인
        chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId)
                //있으면 예외 발생
                .ifPresent(chatRoomMember -> {
                    throw new ChatRoomException(ChatRoomErrorMessage.ALREADY_JOINED.getMessage());
                });

        //chatRoomMember가 없으면
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomException(ChatRoomErrorMessage.CHAT_ROOM_NOT_FOUND.getMessage()));

        Member member = memberRepository.findNotDeletedById(memberId)
                .orElseThrow(() -> new ChatRoomException(MemberErrorMessage.NO_SUCH_MEMBER.getMessage()));

        ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                .sessionId(sessionId)
                .chatRoom(chatRoom)
                .member(member)
                .build();

        chatRoomMemberRepository.save(chatRoomMember);
    }

    @Transactional
    public ChatRoomMember leaveChatRoom(String sessionId) {
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findWithChatRoomAndMemberBySessionId(sessionId)
                .orElseThrow(() -> new ChatRoomException(ChatRoomErrorMessage.NOT_JOINED.getMessage()));

        ChatRoom chatRoom = chatRoomMember.getChatRoom();

        Member member = chatRoomMember.getMember();

        chatRoom.leaveChatRoomMember(chatRoomMember);
        member.leaveChatRoomMember(chatRoomMember);

        chatRoomMemberRepository.delete(chatRoomMember);

        return chatRoomMember;
    }

    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        chatRoomMemberRepository.findByChatRoomId(chatRoomId)
                .forEach(chatRoomMember -> {
                    Member member = memberRepository.findNotDeletedById(chatRoomMember.getMember().getId())
                            .orElseThrow(() -> new ChatRoomException(MemberErrorMessage.NO_SUCH_MEMBER.getMessage()));

                    member.leaveChatRoomMember(chatRoomMember);

                    chatRoomMemberRepository.delete(chatRoomMember);
                });

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomException(ChatRoomErrorMessage.CHAT_ROOM_NOT_FOUND.getMessage()));

        chatRoomRepository.delete(chatRoom);
    }
}
