package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.RegisterInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface RegisterInfoMapper extends BaseMapper<RegisterInfo> {

    /**
     * 分页获取挂号记录信息
     *
     * @param page         分页对象
     * @param registerInfo 挂号记录信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRegisterPage(Page<RegisterInfo> page, @Param("registerInfo") RegisterInfo registerInfo);
}
