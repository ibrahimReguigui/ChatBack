package com.example.wschatapp.controller;

import com.example.wschatapp.entity.Attachment;
import com.example.wschatapp.entity.ResponseDataAttachement;
import com.example.wschatapp.service.AttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*",exposedHeaders = "File-Name")
@RestController
@RequestMapping("/attachment")
@Slf4j
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @PostMapping("/upload")
    public ResponseEntity<List<ResponseDataAttachement>> uploadFile(
            @RequestParam("files") List<MultipartFile> files) throws Exception {

        List<ResponseDataAttachement> res = new ArrayList<>();
        for (MultipartFile file : files) {
            Attachment attachment = attachmentService.saveAttachment(file);
            res.add(new ResponseDataAttachement(attachment.getFileName(),
                    attachment.getId().toString(),
                    file.getContentType(),
                    file.getSize()));
        }

        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) throws Exception {
        Attachment attachment = attachmentService.getAttachment(fileId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", attachment.getFileName());

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(attachment.getFileType()))
                .headers(httpHeaders).body(new ByteArrayResource(attachment.getData()));
    }

}
