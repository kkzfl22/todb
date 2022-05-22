package org.liujun.test.db.plusbatch.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.liujun.test.db.data.entity.UserInfoData;
import org.liujun.test.db.plusbatch.mapper.UserInfoMapper;
import org.liujun.test.db.plusbatch.plus.UserInfoPlusService;
import org.liujun.test.db.plusbatch.utils.BatchExecuteDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 执行数据的保存操作
 *
 * @author liujun
 * @since 2022/5/21
 */
@Repository
@Slf4j
public class UserInfoRepository {

  @Autowired private UserInfoMapper userMapper;

  @Autowired private UserInfoPlusService plusService;

  @Autowired private DataSource dataSource;

  public void clean() {
    userMapper.delete(Wrappers.emptyWrapper());
  }

  /**
   * 最基础的单条插入
   *
   * @param dataList
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  public boolean baseSaveUser(List<UserInfoData> dataList) {
    log.debug("test");
    for (UserInfoData dbItem : dataList) {
      userMapper.insert(dbItem);
    }

    return true;
  }

  /**
   * 最基础的单条插入
   *
   * @param dataList
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  public boolean plusSaveUser(List<UserInfoData> dataList) {
    log.debug("test");
    plusService.saveBatch(dataList);
    return true;
  }

  /**
   * 最基础的单条插入
   *
   * @param dataList
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  public boolean mysqlBatch(List<UserInfoData> dataList) {
    log.debug("test");
    // 分批执行入库操作，并得到响应
    int updateRsp = BatchExecuteDataUtils.batchExecute(userMapper::batchInsert, dataList);

    return true;
  }

  public boolean loaderData(String loadDataSql) {
    try (Connection connect = dataSource.getConnection();
        Statement statement = connect.createStatement(); ) {

      statement.execute(loadDataSql);

      return true;

    } catch (SQLException exception) {
      exception.printStackTrace();
    }

    return false;
  }
}
