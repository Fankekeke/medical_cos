package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.DoctorInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface DoctorInfoMapper extends BaseMapper<DoctorInfo> {

    /**
     * 分页获取医生信息
     *
     * @param page       分页对象
     * @param doctorInfo 医生信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDoctorPage(Page<DoctorInfo> page, @Param("doctorInfo") DoctorInfo doctorInfo);
}
