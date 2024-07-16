package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.ScheduleInfo;
import cc.mrbird.febs.cos.dao.ScheduleInfoMapper;
import cc.mrbird.febs.cos.service.IScheduleInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class ScheduleInfoServiceImpl extends ServiceImpl<ScheduleInfoMapper, ScheduleInfo> implements IScheduleInfoService {

    /**
     * 分页获取排班信息
     *
     * @param page         分页对象
     * @param scheduleInfo 排班信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectSchedulePage(Page<ScheduleInfo> page, ScheduleInfo scheduleInfo) {
        return baseMapper.selectSchedulePage(page, scheduleInfo);
    }

    /**
     * 选择月份生成排班信息
     *
     * @param date 日期
     * @return 结果
     */
    @Override
    public boolean saveBatchSchedule(String date) {
        date = date + "-01";
        // 获取排班模板
        List<ScheduleInfo> scheduleInfoList = this.list(Wrappers.<ScheduleInfo>lambdaQuery().eq(ScheduleInfo::getStatus, "1"));
        if (CollectionUtil.isEmpty(scheduleInfoList)) {
            return true;
        }

        // 获取本月所有日期
        Date beginDate = DateUtil.beginOfMonth(new Date());
        Date endDate = DateUtil.endOfMonth(new Date());
        List<DateTime> dateList = DateUtil.rangeToList(beginDate, endDate, DateField.DAY_OF_YEAR);

        // 待添加的排班信息
        List<ScheduleInfo> toAddScheduleList = new ArrayList<>();
        for (DateTime dateTime : dateList) {
            for (ScheduleInfo scheduleInfo : scheduleInfoList) {
                ScheduleInfo schedule = new ScheduleInfo();
                schedule.setCode(scheduleInfo.getCode());
                schedule.setName(scheduleInfo.getName());
                schedule.setStartDate(scheduleInfo.getStartDate());
                schedule.setEndDate(scheduleInfo.getEndDate());
                schedule.setTaskDate(dateTime.toString("yyyy-MM-dd"));
                schedule.setCreateDate(DateUtil.formatDateTime(new Date()));
                schedule.setStaffIds(scheduleInfo.getStaffIds());
                schedule.setOfficeId(scheduleInfo.getOfficeId());
                schedule.setStatus("0");
                toAddScheduleList.add(schedule);
            }
        }

        return this.saveBatch(toAddScheduleList);
    }
}
