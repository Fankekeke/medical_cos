package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DoctorInfo;
import cc.mrbird.febs.cos.service.IDoctorInfoService;
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
@RequestMapping("/cos/doctor-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoctorInfoController {

    private final IDoctorInfoService doctorInfoService;

    /**
     * 分页获取医生信息
     *
     * @param page       分页对象
     * @param doctorInfo 医生信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DoctorInfo> page, DoctorInfo doctorInfo) {
        return R.ok(doctorInfoService.selectDoctorPage(page, doctorInfo));
    }

    /**
     * 查询医生信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(doctorInfoService.getById(id));
    }

    /**
     * 查询医生信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(doctorInfoService.list());
    }

    /**
     * 新增医生信息
     *
     * @param doctorInfo 医生信息
     * @return 结果
     */
    @PostMapping
    public R save(DoctorInfo doctorInfo) {
        return R.ok(doctorInfoService.save(doctorInfo));
    }

    /**
     * 修改医生信息
     *
     * @param doctorInfo 医生信息
     * @return 结果
     */
    @PutMapping
    public R edit(DoctorInfo doctorInfo) {
        return R.ok(doctorInfoService.updateById(doctorInfo));
    }

    /**
     * 删除医生信息
     *
     * @param ids ids
     * @return 医生信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(doctorInfoService.removeByIds(ids));
    }

}
