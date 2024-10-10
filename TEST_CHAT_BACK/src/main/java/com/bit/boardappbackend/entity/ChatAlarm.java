package com.bit.boardappbackend.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(
        name = "chatAlarmSeqGenerator",
        sequenceName = "CHAT_ALARM_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatAlarm {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chatAlarmSeqGenerator"
    )
    private  Long id;
    private  Long userId;
    private  Long roomId;
    private  int count;
    private  String message;
    
}
