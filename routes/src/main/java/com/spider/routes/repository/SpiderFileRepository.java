package com.spider.routes.repository;

import com.spider.routes.model.SpiderFile;
import com.spider.routes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpiderFileRepository extends JpaRepository<SpiderFile, UUID> {
    List<SpiderFile> findByAuthor(User author);

    List<SpiderFile> findBySolutionFilenameIsNull();
}
