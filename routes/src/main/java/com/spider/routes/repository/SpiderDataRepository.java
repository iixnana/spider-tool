package com.spider.routes.repository;

import com.spider.routes.model.SpiderData;
import com.spider.routes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpiderDataRepository extends JpaRepository<SpiderData, UUID> {
    List<SpiderData> findByAuthor(User author);

    List<SpiderData> findBySolutionFilenameIsNull();
}
