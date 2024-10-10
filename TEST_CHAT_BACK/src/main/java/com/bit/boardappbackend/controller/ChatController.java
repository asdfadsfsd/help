package com.bit.boardappbackend.controller;


import com.bit.boardappbackend.dto.ResponseDto;
import com.bit.boardappbackend.dto.RoomChatDto;
import com.bit.boardappbackend.dto.RoomDto;
import com.bit.boardappbackend.dto.RoomMemberDto;
import com.bit.boardappbackend.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<?> getRoomList(){
        ResponseDto<RoomDto> responseDto = new ResponseDto<>();
        try{
            List<RoomDto> roomDtoList = roomService.getAllRooms();
            responseDto.setItems(roomDtoList);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("get Roooms");
            return ResponseEntity.ok().body(responseDto);
        }catch(Exception e){
            log.error("get error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }




    @PostMapping("/create")
    public ResponseEntity<?> roomCreate(RoomDto roomDto){
        ResponseDto<RoomDto> responseDto = new ResponseDto<>();
        try{
            RoomDto createRoomDto = roomService.createRoom(roomDto);
            responseDto.setItem(createRoomDto);
            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("created");

            return ResponseEntity.ok().body(responseDto);
        }catch(Exception e){
            log.error("get error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }


    @PostMapping("/sendFile/{roomId}")
    public ResponseEntity<?> sendFile(
                         @RequestPart("roomChat") RoomChatDto roomChat,
                         @RequestPart(value = "uploadFiles", required = false) MultipartFile[] uploadFiles
    )
    {
        ResponseDto<RoomDto> responseDto = new ResponseDto<>();
        try{
            roomService.sendFile(roomChat, uploadFiles);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("upload files");
            return ResponseEntity.ok().body(responseDto);
        }catch(Exception e){
            log.error("get error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }



    //Alart등 DB 생각 좀더 해보고
    @GetMapping("/enter/{roomId}")
    public ResponseEntity<?> enterRoom(@PathVariable Long roomId,
                          @RequestParam("userId") Long userId,
                          @RequestParam(value ="last_chat", required = false) RoomChatDto last_chat
    )
    {
        ResponseDto<RoomChatDto> responseDto = new ResponseDto<>();
        try{
            List<RoomChatDto> roomChatDtoList = roomService.enterRoom(roomId,userId,last_chat);
            responseDto.setItems(roomChatDtoList);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("entered room");
            return ResponseEntity.ok().body(responseDto);
        }catch(Exception e){
            log.error("get error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @GetMapping("/alarm")
    public ResponseEntity<?> getChatAlarmList(){
        ResponseDto<RoomDto> responseDto = new ResponseDto<>();
        try{
            //연결 여부 확인후 들어가기
            return null;
        }catch(Exception e){
            log.error("get error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @PatchMapping("/alarm")
    public ResponseEntity<?> readChatAlarm(){
        ResponseDto<RoomDto> responseDto = new ResponseDto<>();
        try{
            //연결 여부 확인후 들어가기
            return null;
        }catch(Exception e){
            log.error("get error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }


    //audio opus n:n
    @GetMapping(value = "/audio-enter")
    public  ResponseEntity<?> audioEnter(RoomMemberDto member){

        ResponseDto<RoomMemberDto> responseDto = new ResponseDto<>();
        try{
            List<RoomMemberDto> roomMemberDtos = roomService.enterAudio(member);
            responseDto.setItems(roomMemberDtos);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("entered audio");
            return ResponseEntity.ok().body(responseDto);
        }catch (Exception e){
            log.error("get error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }

    }



}
