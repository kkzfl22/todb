package org.liujun.test.db.plusbatch.repository;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.liujun.test.db.data.entity.UserInfoData;
import org.liujun.test.db.data.entity.UserInfoDataBuilder;
import org.liujun.test.db.plusbatch.plus.UserInfoPlusService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 测试入库
 *
 * @author liujun
 * @since 2022/5/21
 */
@SpringBootTest(classes = {UserInfoRepository.class})
@Import(
    value = {
      DataSourceAutoConfiguration.class,
      MybatisPlusAutoConfiguration.class,
      // MyBatisScanAuthConfiguration.class,
      UserInfoPlusService.class
    })
@MapperScan("org.liujun.test.db.plusbatch.mapper")
@TestPropertySource("classpath:application.yml")
// @Transactional
public class TestUserInfoRepository {

  @Autowired private UserInfoRepository repository;

  private static final int size = 1000;
  private static final int runSize = 5;

  /** 列分隔符 */
  private static final String COLUMN_SPIT = ",";

  private Map<Integer, List<UserInfoData>> dataListMap = new HashMap<>();

  @BeforeEach
  public void before() {
    System.out.println("当前数据量:" + size);

    repository.clean();

    dataListMap.put(32, UserInfoDataBuilder.builderData(size));
    dataListMap.put(0, UserInfoDataBuilder.builderData(size));
    dataListMap.put(1, UserInfoDataBuilder.builderData(size));
    dataListMap.put(2, UserInfoDataBuilder.builderData(size));
    dataListMap.put(3, UserInfoDataBuilder.builderData(size));
    dataListMap.put(4, UserInfoDataBuilder.builderData(size));
    dataListMap.put(5, UserInfoDataBuilder.builderData(size));
  }

  @Test
  @Disabled
  public void baseInsert() {
    this.baseOperator(32);

    for (int i = 0; i < runSize; i++) {
      run(this::baseOperator, i);
    }
  }

  @Test
  public void plusInsert() {
    this.plusOperator(32);

    for (int i = 0; i < runSize; i++) {
      run(this::plusOperator, i);
    }
  }

  @Test
  public void mysqlInsert() {
    this.mysqlBatchOperator(32);

    for (int i = 0; i < runSize; i++) {
      run(this::mysqlBatchOperator, i);
    }
  }

  @Test
  public void mysqlLoaderData() {
    this.mysqlLoadDataOperator(32);

    for (int i = 0; i < runSize; i++) {
      run(this::mysqlLoadDataOperator, i);
    }
  }

  public void baseOperator(int index) {
    boolean rsp = repository.baseSaveUser(dataListMap.get(index));
    Assertions.assertTrue(rsp);
  }

  public void plusOperator(int index) {
    boolean rsp = repository.plusSaveUser(dataListMap.get(index));
    Assertions.assertTrue(rsp);
  }

  public void mysqlBatchOperator(int index) {
    boolean rsp = repository.mysqlBatch(dataListMap.get(index));
    Assertions.assertTrue(rsp);
  }

  public void mysqlLoadDataOperator(int index) {

    String dataSql = loaderData(index);

    boolean rsp = repository.loaderData(dataSql);
    Assertions.assertTrue(rsp);
  }

  public String loaderData(int index) {
    // 1,将数据写入文件
    // String basePath = TestUserInfoRepository.class.getClassLoader().getResource(".").getPath();
    String basePath = "D:/temp/mysql";

    String outfileName = basePath + "/" + "loaderData.txt";

    toWriteFile(outfileName, dataListMap.get(index));

    return loadSql(outfileName);
  }

  public String loadSql(String path) {
    StringBuilder outSql = new StringBuilder();

    outSql.append("load data infile '");
    outSql.append(path).append("'");
    outSql.append(" into table userinfo ");
    outSql.append("fields terminated by ',' enclosed by '\"' ");
    outSql.append("(id,user_name,pwd,full_name,phone,email,card_id,address,city,create_time)");

    return outSql.toString();
  }

  private void toWriteFile(String file, List<UserInfoData> data) {
    try (FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter); ) {

      for (UserInfoData dataItem : data) {
        bufferedWriter.write(outLine(dataItem));
        bufferedWriter.newLine();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String outLine(UserInfoData data) {
    StringBuilder outData = new StringBuilder();

    outData.append(data.getId()).append(COLUMN_SPIT);
    outData.append(data.getUserName()).append(COLUMN_SPIT);
    outData.append(data.getPwd()).append(COLUMN_SPIT);
    outData.append(data.getFullName()).append(COLUMN_SPIT);
    outData.append(data.getPhone()).append(COLUMN_SPIT);
    outData.append(data.getEmail()).append(COLUMN_SPIT);
    outData.append(data.getCardId()).append(COLUMN_SPIT);
    outData.append(data.getAddress()).append(COLUMN_SPIT);
    outData.append(data.getCity()).append(COLUMN_SPIT);
    outData.append(data.getCreateTime());

    return outData.toString();
  }

  public void run(Consumer<Integer> runMethod, int index) {
    Long startTime = System.nanoTime();
    runMethod.accept(index);
    Long endTime = System.nanoTime();
    long useTime = (endTime - startTime);
    System.out.println(
        "第"
            + index
            + "次，用时（毫秒）:"
            + (useTime / 1000000.0f)
            + ",每条用时(毫秒):"
            + (useTime / size / 1000000.0f));
  }
}
