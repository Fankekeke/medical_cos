package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.UserInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IUserInfoService extends IService<UserInfo> {

    /**
     * 分页获取员工信息
     *
     * @param page     分页对象
     * @param userInfo 员工信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectUserPage(Page<UserInfo> page, @Param("userInfo") UserInfo userInfo);

    /**
     * 根据用户ID获取统计数据
     *
     * @param userId 用户ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectRateByUserId(Integer userId);
}
