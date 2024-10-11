package com.bit.boardappbackend.controller;

import com.bit.boardappbackend.common.ConvertUtils;
import com.bit.boardappbackend.dto.*;
import com.bit.boardappbackend.repository.RoomMemberRepository;
import com.bit.boardappbackend.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebsocketController {

    private final RoomService roomService;
    private final ConvertUtils convertUtils;


    @MessageMapping(value = "/join")
    public void roomJoin(String chat_data) {
        try{
            RoomChatDto chat = convertUtils.stringToDto(chat_data,RoomChatDto.class);
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
    public void chattingSend(String chat_data){
        try{
            RoomChatDto chat = convertUtils.stringToDto(chat_data,RoomChatDto.class);
            roomService.sendMessageToKafka(chat);
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }

    @MessageMapping(value = "/exit")
    public void roomExit(String chat_data){
        try{
            RoomChatDto chat = convertUtils.stringToDto(chat_data,RoomChatDto.class);
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
    public void audioSend( byte[] audioData,
                          @PathVariable("userId") Long userId,
                          @PathVariable("roomId") Long roomId) {
        RoomChatMediaDto data = RoomChatMediaDto.builder()
                .message(audioData)
                .userId(userId)
                .roomId(roomId)
                .build();
        System.out.println("Audio data received and processed");
        roomService.sendAudio(data);
    }
    //video 1:n
    @MessageMapping(value = "/video-send")
    public void videoSend(RoomChatMediaDto data){
        roomService.sendVideo(data);
    }


}
