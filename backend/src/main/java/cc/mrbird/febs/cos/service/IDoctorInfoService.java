package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.DoctorInfo;
import cc.mrbird.febs.cos.entity.ScheduleInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

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

    /**
     * 根据医生ID获取统计数据
     *
     * @param doctorId 医生ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectRateByDoctorId(Integer doctorId);

    /**
     * 根据科室获取医生排班信息
     *
     * @param officeId 科室ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectDoctorByOfficeId(Integer officeId);

    /**
     * 根据科室获取医生排班信息
     *
     * @param officeId 科室ID
     * @return 结果
     */
    List<ScheduleInfo> selectDoctorByOfficeIdFix(Integer officeId);
}
