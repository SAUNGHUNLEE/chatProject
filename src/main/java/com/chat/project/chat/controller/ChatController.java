package com.chat.project.chat.controller;

import com.chat.project.chat.dto.ChatRoomDTO;
import com.chat.project.chat.model.ChatRoom;
import com.chat.project.chat.service.ChatService;
import com.chat.project.chat.service.CustomerUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/login/chat")
public class ChatController {

    private final SimpMessageSendingOperations template;

    private final ChatService chatService;

    @MessageMapping("/chat/enterUser") //pub 생략
    public void enterUser(@Payload ChatRoom chat, SimpMessageHeaderAccessor headerAccessor, @AuthenticationPrincipal CustomerUserDetails userDetails) {
        try {
            if (userDetails.getName().equals(chat.getSender())) {
                // 채팅방 유저+1
                chatService.plusUserCnt(chat.getRoomId());

                // 채팅방에 유저 추가 및 UserUUID 반환
                String userUUID = chatService.addUser(chat.getRoomId(), chat.getSender());

                // 반환 결과를 socket session 에 userUUID 로 저장
                headerAccessor.getSessionAttributes().put("userUUID", userUUID);
                headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

                chat.setMessage(chat.getSender() + " 님 입장!!");
                template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
            } else {
                throw new IllegalArgumentException("로그인된 사용자와 메시지 송신자가 일치하지 않습니다.");
            }
        } catch (IllegalArgumentException e) {
            ChatRoom errorChat = new ChatRoom();
            errorChat.setRoomId(chat.getRoomId());
            errorChat.setType(ChatRoom.MessageType.ERROR);
            errorChat.setMessage("오류: " + e.getMessage());
            template.convertAndSend("/sub/chat/error/" + chat.getRoomId(), errorChat);
        }
    }


    // 해당 유저
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatRoom chat) {
        log.info("CHAT {}", chat);
        chat.setMessage(chat.getMessage());
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);

    }

    // 유저 퇴장 시에는 EventListener 을 통해서 유저 퇴장을 확인
    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("DisConnEvent {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // stomp 세션에 있던 uuid 와 roomId 를 확인해서 채팅방 유저 리스트와 room 에서 해당 유저를 삭제
        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        log.info("headAccessor {}", headerAccessor);

        // 채팅방 유저 -1
        chatService.minusUserCnt(roomId);

        // 채팅방 유저 리스트에서 UUID 유저 닉네임 조회 및 리스트에서 유저 삭제
        String username = chatService.getUserName(roomId, userUUID);
        chatService.deleteUser(roomId, userUUID);

        if (username != null) {
            log.info("User Disconnected : " + username);

            // builder 어노테이션 활용
            ChatRoom chat = ChatRoom.builder()
                    .type(ChatRoom.MessageType.OUT)
                    .sender(username)
                    .message(username + " 님 퇴장!!")
                    .build();

            template.convertAndSend("/sub/chat/room/" + roomId, chat);
        }
    }

    // 채팅에 참여한 유저 리스트 반환
    @GetMapping("/chat/userlist")
    @ResponseBody
    public ArrayList<String> userList(String roomId) {

        return chatService.getUserList(roomId);
    }

    // 채팅에 참여한 유저 닉네임 중복 확인
    @GetMapping("/chat/duplicateName")
    @ResponseBody
    public String isDuplicateName(@RequestParam("roomId") String roomId, @RequestParam("username") String username, @AuthenticationPrincipal CustomerUserDetails userDetails) {

        // 중복되지 않는 이름
        String nonDuplicateName = chatService.isDuplicateName(roomId, userDetails.getName());
        // 유저 이름 확인
        String userName = chatService.isDuplicateName(roomId, nonDuplicateName);
        log.info("동작확인 {}", userName);

        return userName;
    }

}