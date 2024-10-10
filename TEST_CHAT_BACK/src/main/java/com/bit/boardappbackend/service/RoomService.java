package com.bit.boardappbackend.service;


import com.bit.boardappbackend.dto.RoomChatDto;
import com.bit.boardappbackend.dto.RoomChatMediaDto;
import com.bit.boardappbackend.dto.RoomDto;
import com.bit.boardappbackend.dto.RoomMemberDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {
    public List<RoomDto> getAllRooms(); //방 목록 가져오기

    public RoomDto createRoom(RoomDto roomDto); //방 만들기
    public RoomDto changeSettingRoom(RoomDto roomDto); //방 설정 변경
    public List<RoomChatDto> enterRoom(long roomId ,long userId, RoomChatDto LastChat);
    public List<RoomMemberDto> enterAudio(RoomMemberDto roomChatDto);

    public RoomDto joinRoom(long roomId, long userId);
    public void leaveRoom(long roomId, long userId);

    public void sendMessageToKafka(RoomChatDto roomChatDto);  // kafka에 메세지 던지기
    public void sendFile(RoomChatDto roomChatDto ,MultipartFile[] multipartFiles);



    public void startVideo(RoomMemberDto roomChatDto);

    public void sendAudio(RoomChatMediaDto data);
    public void sendVideo(RoomChatMediaDto data);
}
