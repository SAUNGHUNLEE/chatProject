package com.chat.project.chat.service;

import com.chat.project.chat.dto.ChatRoomDTO;
import com.chat.project.chat.model.ChatRoom;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatService {

    private Map<String, ChatRoomDTO> chatRoomMap;
    
    @PostConstruct
    private void init(){
        chatRoomMap = new LinkedHashMap<>();
    }
    
    //전체 채팅방 조회
    public List<ChatRoomDTO> findAllRoom(){
        List chatRooms = new ArrayList(chatRoomMap.values());
        Collections.reverse(chatRooms); //최신순 나오기위해 역순으로
        return chatRooms;
    }

    public ChatRoomDTO findRoomById(String roomId){
        return chatRoomMap.get(roomId);
    }

    public ChatRoomDTO createChatRoomDTO(String roomName){
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO().create(roomName);
        chatRoomMap.put(chatRoomDTO.getRoomId(),chatRoomDTO);
        return chatRoomDTO;
    }

    public void plusUserCnt(String roomId){
        ChatRoomDTO room = chatRoomMap.get(roomId);
        room.setUserCount(room.getUserCount() + 1);
    }

    public void minusUserCnt(String roomId){
        ChatRoomDTO room = chatRoomMap.get(roomId);
        room.setUserCount(room.getUserCount() - 1);
    }

    public String addUser(String roomId, String userName){
        ChatRoomDTO chatRoomDTO = chatRoomMap.get(roomId);
        String userUUID = UUID.randomUUID().toString();

        chatRoomDTO.getUserlist().put(userUUID,userName);
        return userUUID;
    }

    //이름 중복 확인
    public String isDuplicateName(String roomId, String userName){
        ChatRoomDTO chatRoomDTO = chatRoomMap.get(roomId);
        String tmp = userName;

        while(chatRoomDTO.getUserlist().containsValue(tmp)){
            int randomNum = (int) (Math.random()*100)+1;
            tmp = userName + randomNum;
        }
        return tmp;
    }

    public void deleteUser(String roomId, String userUUID){
        ChatRoomDTO chatRoomDTO = chatRoomMap.get(roomId);
        chatRoomDTO.getUserlist().remove(userUUID);
    }

    public String getUserName(String roomID, String userUUID){
        ChatRoomDTO chatRoomDTO = chatRoomMap.get(roomID);
        return chatRoomDTO.getUserlist().get(userUUID);
    }

    //채팅방 전체 회원 조회
    public ArrayList<String> getUserList(String roomId){
        ArrayList<String> list = new ArrayList<>();

        ChatRoomDTO chatRoomDTO = chatRoomMap.get(roomId);
        chatRoomDTO.getUserlist().forEach((key,value) -> list.add(value));
        return list;
    }
}
