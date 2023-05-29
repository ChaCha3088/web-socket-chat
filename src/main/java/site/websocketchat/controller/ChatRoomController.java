package site.websocketchat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import site.websocketchat.config.auth.PrincipalUserDetails;
import site.websocketchat.dto.chat.ChatRoomDto;
import site.websocketchat.enumstorage.errormessage.ChatRoomErrorMessage;
import site.websocketchat.exception.ChatRoomException;
import site.websocketchat.form.ChatRoomCreateForm;
import site.websocketchat.service.ChatRoomMemberService;
import site.websocketchat.service.ChatRoomService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;

    @GetMapping("/chat-room/list")
    public String chatRoomList(Model model) {
        List<ChatRoomDto> allChatRooms = chatRoomService.findAllChatRooms();
        model.addAttribute("allChatRooms", allChatRooms);

        return "chat/chatRoomList";
    }

    @GetMapping("/chat-room/create")
    public String chatRoomCreateForm(Model model) {
        model.addAttribute("chatRoomCreateForm", new ChatRoomCreateForm());

        return "chat/chatRoomCreateForm";
    }

    @PostMapping("/chat-room/create")
    public String createChatRoom(ChatRoomCreateForm chatRoomCreateForm, Model model, BindingResult result) {
        try {
            chatRoomService.createChatRoom(chatRoomCreateForm.getName(), chatRoomCreateForm.getMaxCapacity());
        } catch (ChatRoomException e) {
            if (e.getMessage().equals(ChatRoomErrorMessage.NAME_IS_BLANK.getMessage())) {
                result.addError(new FieldError("chatRoomCreateForm", "name", e.getMessage()));
            }
            if (e.getMessage().equals(ChatRoomErrorMessage.CAPACITY_EXCEEDS_MAX.getMessage())) {
                result.addError(new FieldError("chatRoomCreateForm", "maxCapacity", e.getMessage()));
            } else if (e.getMessage().equals(ChatRoomErrorMessage.CAPACITY_BELOW_MIN.getMessage())) {
                result.addError(new FieldError("chatRoomCreateForm", "maxCapacity", e.getMessage()));
            }

            if (result.hasErrors()) {
                model.addAttribute("chatRoomCreateForm", chatRoomCreateForm);

                return "chat/chatRoomCreateForm";
            }
        }

        return "redirect:/chat-room/list";
    }

    @GetMapping("/chat-room/join/{chatRoomId}")
    public String joinChatRoom(@PathVariable Long chatRoomId, Model model) {
        Long memberId = ((PrincipalUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember().getId();

        try
        {
            chatRoomMemberService.findByChatRoomIdAndMemberId(chatRoomId, memberId);
        }
        //이미 채팅방에 참여한 경우
        catch (ChatRoomException e)
        {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("href", "/chat-room/list");

            return "message/message";
        }

        model.addAttribute("chatRoomDto", chatRoomService.findById(chatRoomId));
        model.addAttribute("memberId", memberId);

        return "chat/chatRoom";
    }

}
