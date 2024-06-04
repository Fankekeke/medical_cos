package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.MedicalInfo;
import cc.mrbird.febs.cos.service.IMedicalInfoService;
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
@RequestMapping("/cos/medical-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalInfoController {

    private final IMedicalInfoService medicalInfoService;

    /**
     * 分页获取医疗资讯信息
     *
     * @param page                分页对象
     * @param medicalInfo 医疗资讯信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<MedicalInfo> page, MedicalInfo medicalInfo) {
        return R.ok(medicalInfoService.selectMedicalPage(page, medicalInfo));
    }

    /**
     * 查询医疗资讯信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(medicalInfoService.getById(id));
    }

    /**
     * 查询医疗资讯信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(medicalInfoService.list());
    }

    /**
     * 新增医疗资讯信息
     *
     * @param medicalInfo 医疗资讯信息
     * @return 结果
     */
    @PostMapping
    public R save(MedicalInfo medicalInfo) {
        medicalInfo.setDate(DateUtil.formatDateTime(new Date()));
        return R.ok(medicalInfoService.save(medicalInfo));
    }

    /**
     * 修改医疗资讯信息
     *
     * @param medicalInfo 医疗资讯信息
     * @return 结果
     */
    @PutMapping
    public R edit(MedicalInfo medicalInfo) {
        return R.ok(medicalInfoService.updateById(medicalInfo));
    }

    /**
     * 删除医疗资讯信息
     *
     * @param ids ids
     * @return 医疗资讯信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(medicalInfoService.removeByIds(ids));
    }
}
