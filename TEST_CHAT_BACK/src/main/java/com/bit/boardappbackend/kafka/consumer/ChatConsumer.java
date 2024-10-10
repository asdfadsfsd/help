package com.bit.boardappbackend.kafka.consumer;


import com.bit.boardappbackend.dto.RoomChatDto;
import com.bit.boardappbackend.entity.ChatAlarm;
import com.bit.boardappbackend.entity.RoomChat;
import com.bit.boardappbackend.entity.RoomMember;
import com.bit.boardappbackend.repository.ChatAlarmRepository;
import com.bit.boardappbackend.repository.RoomChatRepository;
import com.bit.boardappbackend.repository.RoomMemberRepository;
import com.bit.boardappbackend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final RoomRepository roomRepository;
    private  final RoomMemberRepository roomMemberRepository;
    private final RoomChatRepository roomChatRepository;
    private  final ChatAlarmRepository chatAlarmRepository;

    private final RedisTemplate<String, RoomChatDto> redisTemplate;

    //socket으로 보내기
    @KafkaListener(topics = "chat-topic", groupId = "chat_id")
    public void sendMessage(String message) {
        System.out.println("CHAT_SOCKET_CONSUMER: " + message);
        String[] parts = message.split(":", 4);
        Long roomId = Long.parseLong(parts[0]);
        String type = parts[1];
        Long user_id = Long.parseLong(parts[2]);
        String chatMessage = parts[3];
        String jsonMessage = String.format("{\"message\":\"%s\", \"type\":\"%s\", \"user_id\":%d}", chatMessage, type, user_id);
        String way = String.format("/topic/chat/%s",roomId);
        System.out.println("TRY SEND MESSAGE ROOM_ID: " + way);
        messagingTemplate.convertAndSend(way,jsonMessage);
    }

    //채팅 저장용 DB로 보내기
    @KafkaListener(topics = "chat-topic", groupId = "save_id")
    //@CachePut(value = "chat", key = "#roomId")
    public RoomChat saveMessage(String message) {

        // 메시지를 파싱하여 필요한 정보를 추출
        String[] parts = message.split(":", 4);
        Long roomId = Long.parseLong(parts[0]);  // roomId를 추출합니다.
        String type = parts[1];
        Long user_id = Long.parseLong(parts[2]);
        String chatMessage = parts[3];

        // 메세지 타입에 따라 다르게 처리
        if (!type.equals("alarm") && !type.equals("setting")) {

            RoomChat roomChat = roomChatRepository.save(RoomChat.builder()
                    .type(type)
                    .message(chatMessage)
                    .userId(user_id)
                    .room(roomRepository.findById(roomId).orElse(null))
                    .build()
            );
           ListOperations<String, RoomChatDto> listOps = redisTemplate.opsForList();
           listOps.rightPush("chat:"+roomId,roomChat.toDto());

        }

        // 설정이나 알람이면 null을 반환
        if(type.equals("join")) {
            RoomMember roomMember = RoomMember.builder()
                    .userId(user_id)
                    .room(roomRepository.findById(roomId).orElse(null))
                    .isAudio(false)
                    .isVideo(false)
                    .isActive(true)
                    .build();
            roomMemberRepository.save(roomMember);
        }
        if(type.equals("exit")) {
          roomMemberRepository.deleteByRoomIdAndUserId(roomId,user_id);
        }

        return null;
    }

    //alarm용 DB로 보내기
    @KafkaListener(topics = "chat-topic", groupId = "alarm_id")
    public void makeAlarm(String message) {
        String[] parts = message.split(":", 4);
        Long roomId = Long.parseLong(parts[0]);
        String type = parts[1];
        Long user_id = Long.parseLong(parts[2]);
        String chatMessage = parts[3];
        switch (type) {
            case "chat" -> {
                chatAlarmRepository.updateAlarm(roomId, user_id, chatMessage);
                return;
            }
            case "img" -> {
                chatMessage="사진";
                chatAlarmRepository.updateAlarm(roomId, user_id, chatMessage);
                return;
            }
            case "file" -> {
                chatMessage="파일";
                chatAlarmRepository.updateAlarm(roomId, user_id, chatMessage);
            }
        }

    }

}
