package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.MedicationInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IMedicationInfoService extends IService<MedicationInfo> {

    /**
     * 分页获取电子处方信息
     *
     * @param page           分页对象
     * @param medicationInfo 电子处方信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectMedicationPage(Page<MedicationInfo> page, MedicationInfo medicationInfo);
}
