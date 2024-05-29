package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.HospitalInfo;
import cc.mrbird.febs.cos.entity.OfficeInfo;
import cc.mrbird.febs.cos.service.IHospitalInfoService;
import cc.mrbird.febs.cos.service.IOfficeInfoService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/office-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OfficeInfoController {

    private final IOfficeInfoService officeInfoService;

    private final IHospitalInfoService hospitalInfoService;

    /**
     * 分页获取科室信息
     *
     * @param page       分页对象
     * @param officeInfo 科室信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<OfficeInfo> page, OfficeInfo officeInfo) {
        return R.ok(officeInfoService.selectOffocePage(page, officeInfo));
    }

    /**
     * 根据医院ID获取科室信息
     *
     * @param hospitalId 医院ID
     * @return 结果
     */
    @GetMapping("/byhospital/{hospitalId}")
    public R selectListByHospital(@PathVariable("hospitalId") Integer hospitalId) {
        return R.ok(officeInfoService.list(Wrappers.<OfficeInfo>lambdaQuery().eq(OfficeInfo::getHospitalId, hospitalId)));
    }

    /**
     * 设置科室所属医院
     *
     * @return 结果
     */
    @GetMapping("/setOfficeHospital")
    @Transactional(rollbackFor = Exception.class)
    public R setOfficeHospital() {
        // 获取所有科室信息
        List<OfficeInfo> officeInfoList = officeInfoService.list();
        // 所有医院信息
        List<HospitalInfo> hospitalInfoList = hospitalInfoService.list();

        // 医院信息转MAP
        Map<String, Integer> hospitalIdMap = hospitalInfoList.stream().collect(Collectors.toMap(HospitalInfo::getHospitalName, HospitalInfo::getId));

        // 待更新的科室信息
        List<OfficeInfo> toUpdateOfficeInfoList = new LinkedList<>();
        for (OfficeInfo officeInfo : officeInfoList) {
            if (StrUtil.isNotEmpty(officeInfo.getHospitalName())) {
                continue;
            }
            Integer hospitalId = hospitalIdMap.get(officeInfo.getHospitalName());
            if (hospitalId == null) {
                continue;
            }
            officeInfo.setHospitalId(hospitalId);
            toUpdateOfficeInfoList.add(officeInfo);
        }
        return R.ok(officeInfoService.updateBatchById(toUpdateOfficeInfoList));
    }

    /**
     * 查询科室信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(officeInfoService.getById(id));
    }

    /**
     * 查询科室信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(officeInfoService.list());
    }

    /**
     * 新增科室信息
     *
     * @param officeInfo 科室信息
     * @return 结果
     */
    @PostMapping
    public R save(OfficeInfo officeInfo) {
        return R.ok(officeInfoService.save(officeInfo));
    }

    /**
     * 修改科室信息
     *
     * @param officeInfo 科室信息
     * @return 结果
     */
    @PutMapping
    public R edit(OfficeInfo officeInfo) {
        return R.ok(officeInfoService.updateById(officeInfo));
    }

    /**
     * 删除科室信息
     *
     * @param ids ids
     * @return 科室信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(officeInfoService.removeByIds(ids));
    }

}
