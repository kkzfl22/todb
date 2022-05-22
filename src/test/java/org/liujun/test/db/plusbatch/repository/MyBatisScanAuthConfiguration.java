package org.liujun.test.db.plusbatch.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 单元测试的mybatis的配制
 *
 * @author liujun
 * @since 2020/04/18
 */
@Configuration
public class MyBatisScanAuthConfiguration {

  @Bean(name = "testTransactionManager")
  public DataSourceTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }
}
