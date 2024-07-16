package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.ScheduleInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface ScheduleInfoMapper extends BaseMapper<ScheduleInfo> {

    /**
     * 分页获取排班信息
     *
     * @param page         分页对象
     * @param scheduleInfo 排班信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectSchedulePage(Page<ScheduleInfo> page, @Param("scheduleInfo") ScheduleInfo scheduleInfo);

    /**
     * 根据医生获取排班信息
     *
     * @param doctorId 医生ID
     * @param year     年度
     * @param month    月度
     * @return 结果
     */
    List<ScheduleInfo> selectScheduleListByMonth(@Param("doctorId") Integer doctorId, @Param("year") Integer year, @Param("month") Integer month);

    /**
     * 获取今天14天的排班信息
     *
     * @param doctorIds 医生ID
     * @return 结果
     */
    List<ScheduleInfo> selectScheduleListByDay(@Param("doctorIds") List<Integer> doctorIds);
}
