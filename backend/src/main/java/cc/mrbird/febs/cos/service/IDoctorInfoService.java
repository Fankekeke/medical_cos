package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.DoctorInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IDoctorInfoService extends IService<DoctorInfo> {

    /**
     * 分页获取医生信息
     *
     * @param page       分页对象
     * @param doctorInfo 医生信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDoctorPage(Page<DoctorInfo> page, DoctorInfo doctorInfo);
}
