package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.HospitalInfo;
import cc.mrbird.febs.cos.entity.StaffInfo;
import cc.mrbird.febs.cos.service.IHospitalInfoService;
import cc.mrbird.febs.cos.service.IStaffInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/staff-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StaffInfoController {

    private final IStaffInfoService staffInfoService;

    private final IHospitalInfoService hospitalInfoService;

    /**
     * 分页获取员工信息
     *
     * @param page      分页对象
     * @param staffInfo 员工信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<StaffInfo> page, StaffInfo staffInfo) {
        return R.ok(staffInfoService.selectStaffPage(page, staffInfo));
    }

    /**
     * 查询员工信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(staffInfoService.getById(id));
    }

    /**
     * 查询员工信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(staffInfoService.list());
    }

    /**
     * 根据医院获取员工信息
     *
     * @param hospitalId 医院ID
     * @return 结果
     */
    @GetMapping("/selectStaffByHospital/{hospitalId}")
    public R selectStaffByHospital(@PathVariable("hospitalId") Integer hospitalId) {
        return R.ok(staffInfoService.list(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getDeptId, hospitalId)));
    }

    /**
     * 根据医院获取员工信息
     *
     * @param hospitalId 医院ID
     * @return 结果
     */
    @GetMapping("/selectStaffByHospital/user/{hospitalId}")
    public R selectStaffByHospitalUser(@PathVariable("hospitalId") Integer hospitalId) {
        HospitalInfo hospitalInfo = hospitalInfoService.getOne(Wrappers.<HospitalInfo>lambdaQuery().eq(HospitalInfo::getUserId, hospitalId));
        if (hospitalInfo != null) {
            return R.ok(staffInfoService.list(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getDeptId, hospitalInfo.getId())));
        } else {
            return R.ok(Collections.emptyList());
        }
    }

    /**
     * 新增员工信息
     *
     * @param staffInfo 员工信息
     * @return 结果
     */
    @PostMapping
    public R save(StaffInfo staffInfo) {
        staffInfo.setCode("STA-" + System.currentTimeMillis());
        staffInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(staffInfoService.save(staffInfo));
    }

    /**
     * 新增员工信息
     *
     * @param staffInfo 员工信息
     * @return 结果
     */
    @PostMapping("/user")
    public R saveUser(StaffInfo staffInfo) {
        // 医院信息
        HospitalInfo hospitalInfo = hospitalInfoService.getOne(Wrappers.<HospitalInfo>lambdaQuery().eq(HospitalInfo::getUserId, staffInfo.getHospitalId()));
        if (hospitalInfo != null) {
            staffInfo.setDeptId(hospitalInfo.getId());
        }
        staffInfo.setCode("STA-" + System.currentTimeMillis());
        return R.ok(staffInfoService.save(staffInfo));
    }

    /**
     * 修改员工信息
     *
     * @param staffInfo 员工信息
     * @return 结果
     */
    @PutMapping
    public R edit(StaffInfo staffInfo) {
        return R.ok(staffInfoService.updateById(staffInfo));
    }

    /**
     * 删除员工信息
     *
     * @param ids ids
     * @return 员工信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(staffInfoService.removeByIds(ids));
    }
}
