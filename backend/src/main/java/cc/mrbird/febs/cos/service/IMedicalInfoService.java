package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.MedicalInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IMedicalInfoService extends IService<MedicalInfo> {

    /**
     * 分页获取医疗资讯信息
     *
     * @param page                分页对象
     * @param medicalInfo 医疗资讯信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectMedicalPage(Page<MedicalInfo> page, MedicalInfo medicalInfo);
}
