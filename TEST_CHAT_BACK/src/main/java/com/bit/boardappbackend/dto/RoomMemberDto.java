package com.bit.boardappbackend.dto;


import com.bit.boardappbackend.entity.Room;
import com.bit.boardappbackend.entity.RoomMember;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoomMemberDto {
    private  Long id;
    private  Long roomId;
    private  Long userId;

    private  Boolean isActive;
    private  Boolean isAudio;
    private  Boolean isVideo;

    public RoomMember toEntity(Room room) {
        return  RoomMember.builder()
                .userId(userId)
                .isActive(isActive)
                .isAudio(isAudio)
                .isVideo(isVideo)
                .room(room)
                .build();
    }

}
