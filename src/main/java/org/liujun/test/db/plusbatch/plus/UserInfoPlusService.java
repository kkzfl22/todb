package org.liujun.test.db.plusbatch.plus;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.liujun.test.db.data.entity.UserInfoData;
import org.liujun.test.db.plusbatch.mapper.UserInfoMapper;
import org.springframework.stereotype.Repository;

/**
 * @author liujun
 * @since 2022/5/21
 */
@Repository
public class UserInfoPlusService extends ServiceImpl<UserInfoMapper, UserInfoData> {}
