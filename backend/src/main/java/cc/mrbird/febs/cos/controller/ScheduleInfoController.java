package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.ScheduleInfo;
import cc.mrbird.febs.cos.service.IScheduleInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/schedule-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduleInfoController {

    private final IScheduleInfoService scheduleInfoService;

    /**
     * 分页获取排班信息
     *
     * @param page         分页对象
     * @param scheduleInfo 排班信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<ScheduleInfo> page, ScheduleInfo scheduleInfo) {
        return R.ok(scheduleInfoService.selectSchedulePage(page, scheduleInfo));
    }

    /**
     * 查询排班信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(scheduleInfoService.getById(id));
    }

    /**
     * 查询排班信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(scheduleInfoService.list());
    }

    /**
     * 新增排班信息
     *
     * @param scheduleInfo 排班信息
     * @return 结果
     */
    @PostMapping
    public R save(ScheduleInfo scheduleInfo) {
        scheduleInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(scheduleInfoService.save(scheduleInfo));
    }

    /**
     * 修改排班信息
     *
     * @param scheduleInfo 排班信息
     * @return 结果
     */
    @PutMapping
    public R edit(ScheduleInfo scheduleInfo) {
        return R.ok(scheduleInfoService.updateById(scheduleInfo));
    }

    /**
     * 删除排班信息
     *
     * @param ids ids
     * @return 排班信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(scheduleInfoService.removeByIds(ids));
    }
}
