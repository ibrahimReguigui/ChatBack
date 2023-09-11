package com.example.wschatapp.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "Chat_Message")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chatseq")
        @SequenceGenerator(name = "chatseq", sequenceName = "chat_seq", allocationSize = 1)
        private Long id;

        @Column(name = "sender")
        private String sender;

        @Column(name = "recipient")
        private String recipient;

        @Column(name = "content")
        private String content;

        @Column(name = "time")
        private LocalDateTime time;

        @Nullable
        private String url;

        private Boolean read=false;

}
