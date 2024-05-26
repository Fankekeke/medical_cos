package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DeptInfo;
import cc.mrbird.febs.cos.service.IDeptInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
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
@RequestMapping("/cos/dept-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeptInfoController {

    private final IDeptInfoService deptInfoService;

    /**
     * 分页获取科室信息
     *
     * @param page     分页对象
     * @param deptInfo 科室信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DeptInfo> page, DeptInfo deptInfo) {
        return R.ok(deptInfoService.selectDeptPage(page, deptInfo));
    }

    /**
     * 查询科室信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(deptInfoService.getById(id));
    }

    /**
     * 查询科室信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(deptInfoService.list());
    }

    /**
     * 新增科室信息
     *
     * @param deptInfo 科室信息
     * @return 结果
     */
    @PostMapping
    public R save(DeptInfo deptInfo) {
        deptInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(deptInfoService.save(deptInfo));
    }

    /**
     * 修改科室信息
     *
     * @param deptInfo 科室信息
     * @return 结果
     */
    @PutMapping
    public R edit(DeptInfo deptInfo) {
        return R.ok(deptInfoService.updateById(deptInfo));
    }

    /**
     * 删除科室信息
     *
     * @param ids ids
     * @return 科室信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(deptInfoService.removeByIds(ids));
    }
}
