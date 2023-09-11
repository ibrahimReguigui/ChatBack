package com.example.wschatapp.repo;

import com.example.wschatapp.entity.Attachment;
import com.example.wschatapp.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachementRepository extends JpaRepository<Attachment, Long> {
}
