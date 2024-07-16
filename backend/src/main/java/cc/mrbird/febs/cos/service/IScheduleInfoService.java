package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.ScheduleInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IScheduleInfoService extends IService<ScheduleInfo> {

    /**
     * 分页获取排班信息
     *
     * @param page         分页对象
     * @param scheduleInfo 排班信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectSchedulePage(Page<ScheduleInfo> page, ScheduleInfo scheduleInfo);

    /**
     * 选择月份生成排班信息
     *
     * @param date 日期
     * @return 结果
     */
    boolean saveBatchSchedule(String date);
}
