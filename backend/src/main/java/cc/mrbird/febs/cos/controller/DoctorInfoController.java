package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DoctorInfo;
import cc.mrbird.febs.cos.entity.HospitalInfo;
import cc.mrbird.febs.cos.entity.OfficeInfo;
import cc.mrbird.febs.cos.entity.RegisterInfo;
import cc.mrbird.febs.cos.service.IDoctorInfoService;
import cc.mrbird.febs.cos.service.IHospitalInfoService;
import cc.mrbird.febs.cos.service.IOfficeInfoService;
import cc.mrbird.febs.cos.service.IRegisterInfoService;
import cc.mrbird.febs.system.service.UserService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/doctor-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoctorInfoController {

    private final IDoctorInfoService doctorInfoService;

    private final IOfficeInfoService officeInfoService;

    private final IHospitalInfoService hospitalInfoService;

    private final IRegisterInfoService registerInfoService;

    private final UserService userService;

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
     * 根据医院获取医生信息
     *
     * @param hospitalUserId 医院ID
     * @return 结果
     */
    @GetMapping("/list/hospital/user/{hospitalUserId}")
    public R selectDoctorByHospitalUserId(@PathVariable("hospitalUserId") Integer hospitalUserId) {
        // 获取医院信息
        HospitalInfo hospitalInfo = hospitalInfoService.getOne(Wrappers.<HospitalInfo>lambdaQuery().eq(HospitalInfo::getUserId, hospitalUserId));
        return R.ok(doctorInfoService.list(Wrappers.<DoctorInfo>lambdaQuery().eq(DoctorInfo::getHospitalId, hospitalInfo.getId())));
    }

    /**
     * 根据医院获取科室
     *
     * @param hospitalId 医院ID
     * @return 结果
     */
    @GetMapping("/selectOfficeByHospitalId/{hospitalId}")
    public R selectOfficeByHospitalId(@PathVariable("hospitalId") Integer hospitalId) {
        List<OfficeInfo> officeInfoList = officeInfoService.list(Wrappers.<OfficeInfo>lambdaQuery().eq(OfficeInfo::getHospitalId, hospitalId));
        return R.ok(officeInfoList);
    }

    /**
     * 根据科室获取医生排班信息
     *
     * @param officeId 科室ID
     * @return 结果
     */
    @GetMapping("/selectDoctorByOfficeId/{officeId}")
    public R selectDoctorByOfficeId(@PathVariable("officeId") Integer officeId) {
        return R.ok(doctorInfoService.selectDoctorByOfficeIdFix(officeId));
    }

    /**
     * 根据账户ID获取医生信息
     *
     * @param userId 医生账户ID
     * @return 结果
     */
    @GetMapping("/user/detail/{userId}")
    public R selectDoctorDetailByUserId(@PathVariable("userId") Integer userId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("doctor", null);
                put("order", Collections.emptyList());
            }
        };
        // 医生信息
        DoctorInfo doctorInfo = doctorInfoService.getOne(Wrappers.<DoctorInfo>lambdaQuery().eq(DoctorInfo::getUserId, userId));
        result.put("doctor", doctorInfo);
        // 排班信息
        List<RegisterInfo> registerInfoList = registerInfoService.list(Wrappers.<RegisterInfo>lambdaQuery().eq(RegisterInfo::getStaffId, doctorInfo.getId()));
        result.put("order", registerInfoList);
        return R.ok(result);
    }

    /**
     * 根据医生ID获取统计数据
     *
     * @param doctorId 医生ID
     * @return 结果
     */
    @GetMapping("/selectRateByDoctorId/{doctorId}")
    public R selectRateByDoctorId(@PathVariable("doctorId") Integer doctorId) {
        return R.ok(doctorInfoService.selectRateByDoctorId(doctorId));
    }

    /**
     * 根据账户ID获取医生信息
     *
     * @param userId 医生账户ID
     * @return 结果
     */
    @GetMapping("/user/{userId}")
    public R selectDoctorByUserId(@PathVariable("userId") Integer userId) {
        return R.ok(doctorInfoService.getOne(Wrappers.<DoctorInfo>lambdaQuery().eq(DoctorInfo::getUserId, userId)));
    }

    /**
     * 根据医院获取医生信息
     *
     * @param hospitalId 医院ID
     * @return 结果
     */
    @GetMapping("/list/hospital/{hospitalId}")
    public R selectDoctorByHospitalId(@PathVariable("hospitalId") Integer hospitalId) {
        return R.ok(doctorInfoService.list(Wrappers.<DoctorInfo>lambdaQuery().eq(DoctorInfo::getHospitalId, hospitalId)));
    }

    /**
     * 设置医生科室及医院信息
     *
     * @return 结果
     */
    @Async
    @GetMapping("/setDoctorInfo")
    @Transactional(rollbackFor = Exception.class)
    public R setDoctorInfo() {
        // 医生信息
        List<DoctorInfo> doctorInfoList = doctorInfoService.list();

        // 科室信息
        List<OfficeInfo> officeInfoList = officeInfoService.list();

        // 医院信息
        List<HospitalInfo> hospitalInfoList = hospitalInfoService.list();

        // 各医院下的科室
        Map<Integer, List<OfficeInfo>> officeByHospitalMap = officeInfoList.stream().filter(e -> e.getHospitalId() != null).collect(Collectors.groupingBy(OfficeInfo::getHospitalId));
        // 医院信息转MAP
        Map<String, List<HospitalInfo>> hospitalInfoMap = hospitalInfoList.stream().collect(Collectors.groupingBy(HospitalInfo::getHospitalName));

        // 待更新的医生信息
        List<DoctorInfo> toUpdateList = new LinkedList<>();
        // 循环设置科室医院ID
        for (DoctorInfo doctorInfo : doctorInfoList) {
            if (StrUtil.isEmpty(doctorInfo.getHospitalName())) {
                continue;
            }
            // 获取医院ID
            List<HospitalInfo> thisHospitalInfoList = hospitalInfoMap.get(doctorInfo.getHospitalName());
            if (CollectionUtil.isEmpty(thisHospitalInfoList)) {
                continue;
            }
            // 绑定所属医院
            doctorInfo.setHospitalId(thisHospitalInfoList.get(0).getId());
            // 获取医院科室信息
            List<OfficeInfo> officeInfoListByHospital = officeByHospitalMap.get(thisHospitalInfoList.get(0).getId());
            if (StrUtil.isEmpty(doctorInfo.getOfficesName()) || CollectionUtil.isEmpty(officeInfoListByHospital)) {
                toUpdateList.add(doctorInfo);
                continue;
            }
            Map<String, List<OfficeInfo>> officesMap = officeInfoListByHospital.stream().collect(Collectors.groupingBy(OfficeInfo::getOfficesName));

            // 获取科室ID
            List<OfficeInfo> checkOffices = officesMap.get(doctorInfo.getOfficesName());
            if (CollectionUtil.isEmpty(checkOffices)) {
                toUpdateList.add(doctorInfo);
                continue;
            }
            doctorInfo.setOfficesId(checkOffices.get(0).getId());
            toUpdateList.add(doctorInfo);
        }
        return R.ok(doctorInfoService.updateBatchById(toUpdateList));
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
    @Transactional(rollbackFor = Exception.class)
    public R save(DoctorInfo doctorInfo) throws Exception {
        doctorInfo.setCode("DT-" + System.currentTimeMillis());
        userService.registDoctor(doctorInfo.getCode(), "1234qwer", doctorInfo);
        return R.ok(true);
    }

    /**
     * 新增医生信息
     *
     * @param doctorInfo 医生信息
     * @return 结果
     */
    @PostMapping("/hospital")
    @Transactional(rollbackFor = Exception.class)
    public R saveByHospital(DoctorInfo doctorInfo) throws Exception {
        // 医院信息
        HospitalInfo hospitalInfo = hospitalInfoService.getOne(Wrappers.<HospitalInfo>lambdaQuery().eq(HospitalInfo::getUserId, doctorInfo.getHospitalId()));
        if (hospitalInfo != null) {
            doctorInfo.setHospitalName(hospitalInfo.getHospitalName());
            doctorInfo.setHospitalId(hospitalInfo.getId());
        }
        doctorInfo.setCode("DT-" + System.currentTimeMillis());
        userService.registDoctor(doctorInfo.getCode(), "1234qwer", doctorInfo);
        return R.ok(true);
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
