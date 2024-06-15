package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.HospitalInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface HospitalInfoMapper extends BaseMapper<HospitalInfo> {

    /**
     * 分页获取医院信息
     *
     * @param page     分页对象
     * @param hospitalInfo 医院信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectHospitalPage(Page<HospitalInfo> page, @Param("hospitalInfo") HospitalInfo hospitalInfo);

    /**
     * 查询医院地图
     *
     * @param key 关键字
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectHospitalByMap(@Param("key")String key);
}
