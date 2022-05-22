package org.liujun.test.db.plusbatch.mapper;

import org.liujun.test.db.data.entity.UserInfoData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 数据库影射操作
 *
 * @author liujun
 * @since 2022/5/21
 */
public interface UserInfoMapper extends BaseMapper<UserInfoData> {

  /**
   * 批量插入
   *
   * @param list 批量集合
   * @return 影响行数
   */
  int batchInsert(List<UserInfoData> list);
}
