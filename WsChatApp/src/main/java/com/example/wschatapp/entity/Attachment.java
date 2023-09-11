package com.example.wschatapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "Attachment")
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATTACHEMENT_SEQ")
        @SequenceGenerator(name = "ATTACHEMENT_SEQ", sequenceName = "ATTACHEMENT_SEQ", allocationSize = 1)
        private Long id;

        @Column(name = "time")
        private LocalDateTime time;

        private String fileName;

        private String fileType;

        @Lob
        private byte[] data;
}
