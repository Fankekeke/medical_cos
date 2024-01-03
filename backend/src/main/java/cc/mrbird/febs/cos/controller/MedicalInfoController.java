package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.MedicalInfo;
import cc.mrbird.febs.cos.service.IMedicalInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 分页获取医疗咨询信息
     *
     * @param page        分页对象
     * @param medicalInfo 医疗咨询信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<MedicalInfo> page, MedicalInfo medicalInfo) {
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
        return R.ok(medicalInfoService.getById(id));
    }

    /**
     * 获取信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(medicalInfoService.list());
    }

    /**
     * 新增医疗咨询信息
     *
     * @param medicalInfo 医疗咨询信息
     * @return 结果
     */
    @PostMapping
    public R save(MedicalInfo medicalInfo) {
        return R.ok(medicalInfoService.save(medicalInfo));
    }

    /**
     * 修改医疗咨询信息
     *
     * @param medicalInfo 医疗咨询信息
     * @return 结果
     */
    @PutMapping
    public R edit(MedicalInfo medicalInfo) {
        return R.ok(medicalInfoService.updateById(medicalInfo));
    }

    /**
     * 删除医疗咨询信息
     *
     * @param ids ids
     * @return 医疗咨询信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(medicalInfoService.removeByIds(ids));
    }
}
