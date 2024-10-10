package com.bit.boardappbackend.controller;

import com.bit.boardappbackend.dto.*;
import com.bit.boardappbackend.repository.RoomMemberRepository;
import com.bit.boardappbackend.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebsocketController {

    private final RoomService roomService;



    @MessageMapping(value = "/join")
    public void roomJoin(RoomChatDto chat) {
        try{
            System.out.println("chat");
            System.out.println(chat);
            roomService.joinRoom(chat.getRoomId(), chat.getUserId());
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }


    @MessageMapping(value = "/enter")
    public void roomEnter(String message){
        try{
          //roomService.();
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }

    @MessageMapping(value = "/send")
    public void chattingSend(RoomChatDto chat){
        try{
            roomService.sendMessageToKafka(chat);
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }

    @MessageMapping(value = "/exit")
    public void roomExit(RoomChatDto chat){
        try{
            roomService.leaveRoom(chat.getRoomId(), chat.getUserId());
        }catch(Exception e){
             log.error(e.getMessage());
        }
    }

    @MessageMapping(value = "/setting")
    public void roomSetting(String message){
        try{
            //roomService.changeSettingRoom();
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }



    //audio opus n:n


    @MessageMapping(value = "/audio-send/{roomId}/{userId}")
    public void audioSend(@Payload byte[] audioData,
                          @PathVariable("userId") Long userId,
                          @PathVariable("roomId") Long roomId){
        System.out.println("132213");
        RoomChatMediaDto data = RoomChatMediaDto.builder()
                .message(audioData)
                .userId(userId)
                .roomId(roomId)
                .build();
        System.out.println("132213");
        roomService.sendAudio(data);
    }

    //video 1:n
    @MessageMapping(value = "/video-send")
    public void videoSend(RoomChatMediaDto data){
        roomService.sendVideo(data);
    }
}
