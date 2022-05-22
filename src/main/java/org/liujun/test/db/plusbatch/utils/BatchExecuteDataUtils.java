package org.liujun.test.db.plusbatch.utils;

import java.util.List;
import java.util.function.ToIntFunction;

/**
 * 批量执行数据库操作
 *
 * @author liujun
 * @version 0.0.1
 */
public class BatchExecuteDataUtils {

  private BatchExecuteDataUtils() {}

  /** 批量入库的最次最大数量 */
  private static int batchNum = 1024;

  /**
   * 此方法用于批次执行数据库的增删改操作
   *
   * @param runFunction
   * @param attributeList
   * @return
   */
  public static <T> int batchExecute(ToIntFunction<List<T>> runFunction, List<T> attributeList) {
    if (null == attributeList || attributeList.isEmpty()) {
      return 0;
    }

    int sumNum = attributeList.size() % batchNum;
    if (sumNum != 0) {
      sumNum = attributeList.size() / batchNum + 1;
    } else {
      sumNum = attributeList.size() / batchNum;
    }

    int addResult = 0;
    for (int i = 0; i < sumNum; i++) {
      int startIndex = i * batchNum;
      int endIndex = (i + 1) * batchNum;
      if (endIndex > attributeList.size()) {
        endIndex = attributeList.size();
      }
      // 获取数据
      List<T> runData = attributeList.subList(startIndex, endIndex);
      // 数据的批量插入
      addResult += runFunction.applyAsInt(runData);
    }

    return addResult;
  }
}
