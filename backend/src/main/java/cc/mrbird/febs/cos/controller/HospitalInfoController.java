package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.HospitalInfo;
import cc.mrbird.febs.cos.service.IHospitalInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/hospital-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HospitalInfoController {

    private final IHospitalInfoService hospitalInfoService;

    /**
     * 分页获取医院信息
     *
     * @param page         分页对象
     * @param hospitalInfo 医院信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<HospitalInfo> page, HospitalInfo hospitalInfo) {
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
        return R.ok(hospitalInfoService.getById(id));
    }

    /**
     * 获取信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(hospitalInfoService.list());
    }

    /**
     * 新增医院信息
     *
     * @param hospitalInfo 医院信息
     * @return 结果
     */
    @PostMapping
    public R save(HospitalInfo hospitalInfo) {
        return R.ok(hospitalInfoService.save(hospitalInfo));
    }

    /**
     * 修改医院信息
     *
     * @param hospitalInfo 医院信息
     * @return 结果
     */
    @PutMapping
    public R edit(HospitalInfo hospitalInfo) {
        return R.ok(hospitalInfoService.updateById(hospitalInfo));
    }

    /**
     * 删除医院信息
     *
     * @param ids ids
     * @return 医院信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(hospitalInfoService.removeByIds(ids));
    }
}
