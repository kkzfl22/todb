/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package org.liujun.test.db;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 项目入口
 *
 * @author 86177
 */
@SpringBootApplication
@MapperScan(value = {"org.liujun.test.db.plusbatch.mapper"})
@Slf4j
@EnableTransactionManagement
public class DbApplication {

  public static void main(String[] args) {

    try {
      ConfigurableApplicationContext application =
          new SpringApplicationBuilder(DbApplication.class)
              .main(DbApplication.class)
              .build()
              .run(args);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
