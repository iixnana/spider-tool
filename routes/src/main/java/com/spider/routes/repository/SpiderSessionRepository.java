package com.spider.routes.repository;

import com.spider.routes.model.SpiderSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpiderSessionRepository extends JpaRepository<SpiderSession, Long> {
    List<SpiderSession> findByIsReadyIsTrueAndOptimizationIsRunningIsFalseAndIterationCountIs(int count);

    List<SpiderSession> findByIsAwaitingOptimizationIsTrue();

    List<SpiderSession> findByIsAwaitingOptimizationIsFalseAndIsDownloadedIsFalse();

    List<SpiderSession> findByIsReadyIsFalse();
}
