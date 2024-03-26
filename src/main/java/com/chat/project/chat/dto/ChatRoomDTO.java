package com.chat.project.chat.dto;

import lombok.*;

import java.util.HashMap;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDTO {
    // 메시지  타입 : 입장, 채팅 , 나가기
    public enum MessageType{
        ENTER, TALK, OUT;
    }

    private String roomId;
    private String roomName;
    private long userCount;//채팅방 인원수

    private HashMap<String,String> userlist = new HashMap<String,String>();

    public ChatRoomDTO create(String roomName){
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.roomId = UUID.randomUUID().toString();
        chatRoomDTO.roomName = roomName;
        return chatRoomDTO;
    }
}
