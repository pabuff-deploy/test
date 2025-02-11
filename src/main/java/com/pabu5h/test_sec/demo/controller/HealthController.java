package com.pabu5h.test_sec.demo.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Getter
public class HealthController {
    private volatile boolean maintenance;

    @Value("${service.version}")
    private String version;

    @GetMapping("/hello")
    public ResponseEntity<String> hello(/*HttpServletResponse httpServletResponse*/) {
        return ResponseEntity.ok()./*headers(headers).*/body("Test:" + version);
    }
    @GetMapping("/health")
    public String checkHealth() {
//        System.out.println("Health check called at " + LocalDateTime.now() + " and maintenance is " + maintenance);
        return maintenance ? "DOWN" : "UP";
    }
    @Scheduled(cron = "${maintenance.start.cron}")
    public void startMaintenance() {
        maintenance = true;
    }

    @Scheduled(cron = "${maintenance.stop.cron}")
    public void stopMaintenance() {
        maintenance = false;
    }
}