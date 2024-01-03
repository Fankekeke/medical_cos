package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.OfficeInfo;
import cc.mrbird.febs.cos.service.IOfficeInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/office-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OfficeInfoController {

    private final IOfficeInfoService officeInfoService;

    /**
     * 分页获取科室管理信息
     *
     * @param page       分页对象
     * @param officeInfo 科室管理信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<OfficeInfo> page, OfficeInfo officeInfo) {
        return R.ok();
    }

    /**
     * 获取详情信息
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(officeInfoService.getById(id));
    }

    /**
     * 获取信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(officeInfoService.list());
    }

    /**
     * 新增科室管理信息
     *
     * @param officeInfo 科室管理信息
     * @return 结果
     */
    @PostMapping
    public R save(OfficeInfo officeInfo) {
        return R.ok(officeInfoService.save(officeInfo));
    }

    /**
     * 修改科室管理信息
     *
     * @param officeInfo 科室管理信息
     * @return 结果
     */
    @PutMapping
    public R edit(OfficeInfo officeInfo) {
        return R.ok(officeInfoService.updateById(officeInfo));
    }

    /**
     * 删除科室管理信息
     *
     * @param ids ids
     * @return 科室管理信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(officeInfoService.removeByIds(ids));
    }
}
