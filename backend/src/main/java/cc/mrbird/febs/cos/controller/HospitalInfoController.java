package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.HospitalInfo;
import cc.mrbird.febs.cos.entity.RegisterInfo;
import cc.mrbird.febs.cos.service.IHospitalInfoService;
import cc.mrbird.febs.cos.service.IRegisterInfoService;
import cc.mrbird.febs.system.service.UserService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/hospital-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HospitalInfoController {

    private final IHospitalInfoService hospitalInfoService;

    private final IRegisterInfoService registerInfoService;

    private final UserService userService;

    /**
     * 分页获取医院信息
     *
     * @param page         分页对象
     * @param hospitalInfo 医院信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<HospitalInfo> page, HospitalInfo hospitalInfo) {
        return R.ok(hospitalInfoService.selectHospitalPage(page, hospitalInfo));
    }

    /**
     * 医院统计
     *
     * @param type 1.按区域统计 2.按医院等级统计 3.按医院类型统计
     * @return 结果
     */
    @GetMapping("/selectStatisticsByType/{type}")
    public R selectStatisticsByType(@PathVariable(value = "type", required = false) Integer type) {
        if (type == null) {
            type = 1;
        }
        return R.ok(hospitalInfoService.selectStatisticsByType(type));
    }

    /**
     * 查询医院地图
     *
     * @return 结果
     */
    @GetMapping("/hospital/map")
    public R selectHospitalByMap() {
        return R.ok(hospitalInfoService.selectHospitalByMap(null));
    }

    /**
     * 逆地址解析
     *
     * @return 结果
     */
    @Async
    @GetMapping("/addressResolve")
    public R addressResolve() {
        hospitalInfoService.addressResolve();
        return R.ok(true);
    }

    /**
     * 医院信息列表
     *
     * @param key 关键词
     * @return 结果
     */
    @GetMapping(("/query/like/list"))
    public R selectHospitalByKey(@RequestParam(value = "key", required = false) String key) {
        return R.ok(hospitalInfoService.list(Wrappers.<HospitalInfo>lambdaQuery().like(StrUtil.isNotEmpty(key), HospitalInfo::getHospitalName, key)));
    }

    /**
     * 根据用户ID获取医院信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/user/detail/{userId}")
    public R selectDetailByUserId(@PathVariable("userId") Integer userId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        HospitalInfo hospitalInfo = hospitalInfoService.getOne(Wrappers.<HospitalInfo>lambdaQuery().eq(HospitalInfo::getUserId, userId));
        result.put("hospital", hospitalInfo);
        List<RegisterInfo> registerInfoList = registerInfoService.list(Wrappers.<RegisterInfo>lambdaQuery().eq(RegisterInfo::getDeptId, hospitalInfo.getId()));
        result.put("order", registerInfoList);
        return R.ok(result);
    }

    /**
     * 查询医院信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(hospitalInfoService.getById(id));
    }

    /**
     * 查询医院信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(hospitalInfoService.list());
    }

    /**
     * 查询医院信息列表
     *
     * @return 结果
     */
    @GetMapping("/list/key/{key}")
    public R remoteList(@PathVariable(value = "key", required = false) String key) {
        return R.ok(hospitalInfoService.list(Wrappers.<HospitalInfo>lambdaQuery().like(StrUtil.isNotEmpty(key), HospitalInfo::getHospitalName, key).last("limit 20")));
    }

    /**
     * 医院统计
     *
     * @param type 类型 1.地区统计 2.医院类型 3.医院等级
     * @return 结果
     */
    @GetMapping("/selectHospitalRate/{type}")
    public R selectHospitalRate(@PathVariable("type") Integer type) {
        return R.ok(hospitalInfoService.selectHospitalRate(type));
    }

    /**
     * 新增医院信息
     *
     * @param hospitalInfo 医院信息
     * @return 结果
     */
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public R save(HospitalInfo hospitalInfo) throws Exception {
        hospitalInfo.setCode("HPL-" + System.currentTimeMillis());
        userService.registHospital(hospitalInfo.getCode(), "1234qwer", hospitalInfo);
        return R.ok(true);
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
