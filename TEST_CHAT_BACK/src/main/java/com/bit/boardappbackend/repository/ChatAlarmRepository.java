package com.bit.boardappbackend.repository;

import com.bit.boardappbackend.entity.ChatAlarm;
import com.bit.boardappbackend.entity.Room;
import com.bit.boardappbackend.entity.RoomChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatAlarmRepository extends JpaRepository<ChatAlarm, Long> {

    @Modifying
    @Query("UPDATE ChatAlarm c SET c.message = :message, c.count = c.count + 1 " +
            "WHERE c.roomId = :roomId AND c.userId IN " +
            "(SELECT rm.userId FROM RoomMember rm WHERE rm.room.id = :roomId AND rm.isActive = false) " +
            "AND c.userId != :userId")
    void updateAlarm(long roomId, long userId, String message);
}
