package com.chat.project.chat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_room")
public class ChatRoom {
    public enum MessageType {
        ENTER, TALK, OUT,ERROR // 환영 인사 , 말하는 사람, 나가는 사람,에러
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Enumerated(EnumType.STRING) // 열거형을 문자열로 데이터베이스에 저장
    @Column(name = "type")
    private MessageType type; // Enum 타입을 MessageType으로 변경


    @Column(name = "room_id")
    private String roomId;

    @Column(name = "sender") //유저 name 이랑 동일
    private String sender;

    @Column(name = "message")
    private String message;

    @Column(name = "time")
    private LocalDateTime  time;

    @Column(name = "room_name")
    private String roomName;



}

