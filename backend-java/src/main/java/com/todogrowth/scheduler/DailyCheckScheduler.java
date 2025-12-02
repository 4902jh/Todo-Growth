package com.todogrowth.scheduler;

import com.todogrowth.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyCheckScheduler {
    private final GameService gameService;

    // 매일 자정에 실행 (cron 표현식: 초 분 시 일 월 요일)
    @Scheduled(cron = "0 0 0 * * *")
    public void checkDailyFailures() {
        log.info("일일 Todo 실패 체크 시작...");
        
        try {
            var results = gameService.checkDailyFailures();
            log.info("처리 완료: {}개의 Todo 실패 처리", results.size());
            
            // 레벨업 발생한 경우 로깅 (실패해도 EXP >= 20이면 레벨업 가능)
            results.stream()
                .filter(r -> r.getLeveledUp() != null && r.getLeveledUp())
                .forEach(r -> log.info("레벨업 발생 (실패 처리 중): {}", r));
                
        } catch (Exception e) {
            log.error("일일 실패 체크 중 오류 발생", e);
        }
    }
}

