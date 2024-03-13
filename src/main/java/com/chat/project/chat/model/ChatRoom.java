package com.chat.project.chat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_room")
public class ChatRoom {
    public enum MessageType {
        ENTER, TALK, LEAVE // 예시로 사용할 수 있는 메시지 타입
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

    @Column(name = "sender")
    private String sender;

    @Column(name = "message")
    private String message;

    @Column(name = "time")
    private LocalDateTime  time;

    @Column(name = "room_name")
    private String roomName;


}
