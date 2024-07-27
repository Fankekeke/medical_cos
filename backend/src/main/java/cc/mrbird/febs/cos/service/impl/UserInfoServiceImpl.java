package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.RegisterInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.dao.UserInfoMapper;
import cc.mrbird.febs.cos.service.IBulletinInfoService;
import cc.mrbird.febs.cos.service.IRegisterInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    private final IBulletinInfoService bulletinInfoService;

    private final IRegisterInfoService registerInfoService;

    /**
     * 分页获取员工信息
     *
     * @param page     分页对象
     * @param userInfo 员工信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectUserPage(Page<UserInfo> page, UserInfo userInfo) {
        return baseMapper.selectUserPage(page, userInfo);
    }

    /**
     * 根据用户ID获取统计数据
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectRateByUserId(Integer userId) {
        // 返回数据
        UserInfo userInfo = this.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("user", userInfo);
                put("bulletin", bulletinInfoService.list());
                put("register", Collections.emptyList());
            }
        };
        // 获取用户挂号信息
        List<RegisterInfo> registerInfoList = registerInfoService.list(Wrappers.<RegisterInfo>lambdaQuery().eq(RegisterInfo::getUserId, userInfo.getId()));
        result.put("register", registerInfoList);
        return result;
    }
}
