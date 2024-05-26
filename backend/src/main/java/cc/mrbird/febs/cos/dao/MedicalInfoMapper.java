package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.MedicalInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface MedicalInfoMapper extends BaseMapper<MedicalInfo> {

    /**
     * 分页获取医疗资讯信息
     *
     * @param page                分页对象
     * @param medicalInfo 医疗资讯信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectMedicalPage(Page<MedicalInfo> page, @Param("medicalInfo") MedicalInfo medicalInfo);
}
