package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.ScheduleInfo;
import cc.mrbird.febs.cos.dao.ScheduleInfoMapper;
import cc.mrbird.febs.cos.service.IScheduleInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

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
}
