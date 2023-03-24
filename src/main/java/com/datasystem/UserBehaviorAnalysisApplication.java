package com.datasystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Pauluooooo
 * Date:2023/3/7 15:28
 */

@SpringBootApplication
@MapperScan("com.datasystem.mapper")

public class UserBehaviorAnalysisApplication {
  public static void main(String[] args) {
    SpringApplication.run(UserBehaviorAnalysisApplication.class, args);
  }
}
