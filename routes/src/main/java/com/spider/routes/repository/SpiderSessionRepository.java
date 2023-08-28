package com.spider.routes.repository;

import com.spider.routes.model.SpiderSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpiderSessionRepository extends JpaRepository<SpiderSession, Long> {
}
