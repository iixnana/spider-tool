package com.spider.routes.repository;

import com.spider.routes.model.SpiderData;
import com.spider.routes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpiderDataRepository extends JpaRepository<SpiderData, Long> {
    List<SpiderData> findByAuthor(User author);

    List<SpiderData> findBySolutionFilenameIsNull();

    List<SpiderData> findBySessionIsNull();
}
