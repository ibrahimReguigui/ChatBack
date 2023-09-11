package com.example.wschatapp.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDataAttachement {

    private String fileName;
    private String downloadURL;
    private String fileType;
    private long fileSize;
}
