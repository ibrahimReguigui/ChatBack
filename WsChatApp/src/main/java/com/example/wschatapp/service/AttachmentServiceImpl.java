package com.example.wschatapp.service;


import com.example.wschatapp.entity.Attachment;
import com.example.wschatapp.repo.AttachementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class AttachmentServiceImpl implements AttachmentService{

    @Autowired
    private AttachementRepository attachmentRepository;


    @Override
    public Attachment saveAttachment(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")) {
                throw  new Exception("Filename contains invalid path sequence "
                        + fileName);
            }
            System.out.println("type : "+file.getContentType());
            Attachment attachment
                    =Attachment.builder().data(file.getBytes()).fileType(file.getContentType())
                    .fileName(fileName).time(LocalDateTime.now())
                    .build();
            return attachmentRepository.save(attachment);

        } catch (Exception e) {
            throw new Exception("Could not save File: " + fileName);
        }
    }

    @Override
    public Attachment getAttachment(Long fileId) throws Exception {
        return attachmentRepository
                .findById(fileId)
                .orElseThrow(
                        () -> new Exception("File not found with Id: " + fileId));
    }
}
