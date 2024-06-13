package com.sparta.igeomubwotna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // Spring의 스케쥴링(미리 정한시간에 자동으로 작업 실행) 활성화
@SpringBootApplication
public class IgeoMubwotnaApplication {
    public static void main(String[] args) {
        SpringApplication.run(IgeoMubwotnaApplication.class, args);
    }

}
