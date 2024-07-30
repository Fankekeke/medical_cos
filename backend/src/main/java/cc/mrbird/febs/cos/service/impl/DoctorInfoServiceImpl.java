package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.RegisterInfoMapper;
import cc.mrbird.febs.cos.dao.ScheduleInfoMapper;
import cc.mrbird.febs.cos.entity.DoctorInfo;
import cc.mrbird.febs.cos.dao.DoctorInfoMapper;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.RegisterInfo;
import cc.mrbird.febs.cos.entity.ScheduleInfo;
import cc.mrbird.febs.cos.service.IDoctorInfoService;
import cc.mrbird.febs.cos.service.IRegisterInfoService;
import cc.mrbird.febs.cos.service.IScheduleInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoctorInfoServiceImpl extends ServiceImpl<DoctorInfoMapper, DoctorInfo> implements IDoctorInfoService {

    private final RegisterInfoMapper registerInfoMapper;

    private final ScheduleInfoMapper scheduleInfoMapper;

    /**
     * 分页获取医生信息
     *
     * @param page       分页对象
     * @param doctorInfo 医生信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectDoctorPage(Page<DoctorInfo> page, DoctorInfo doctorInfo) {
        return baseMapper.selectDoctorPage(page, doctorInfo);
    }

    /**
     * 根据医生ID获取统计数据
     *
     * @param doctorId 医生ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectRateByDoctorId(Integer doctorId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("scheduleNum", 0);
                put("registerNum", 0);
                put("drugTotal", BigDecimal.ZERO);
                put("registerTotal", BigDecimal.ZERO);
                put("series", Collections.emptyList());
                put("series1", Collections.emptyList());
            }
        };
        Integer year = DateUtil.thisYear();
        Integer month = DateUtil.thisMonth() + 1;
        // 本月挂号信息
        List<RegisterInfo> registerInfoList = registerInfoMapper.selectRegisterByMonthDoctor(doctorId, year, month);
        // 本月排班信息
        List<ScheduleInfo> scheduleInfoList = scheduleInfoMapper.selectScheduleListByMonth(doctorId, year, month);

        // 本月排班量
        result.put("scheduleNum", scheduleInfoList.size());
        // 本月总挂号
        result.put("registerNum", registerInfoList.size());
        // 本月处方金额
        BigDecimal drugTotal = registerInfoList.stream().map(RegisterInfo::getDrugPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("drugTotal", drugTotal);
        // 本月挂号金额
        BigDecimal registerTotal = registerInfoList.stream().map(RegisterInfo::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("registerTotal", registerTotal);

        // 挂号数量统计
        List<LinkedHashMap<String, Object>> registerDay = registerInfoMapper.selectRegisterRateByDay();
        List<LinkedHashMap<String, Object>> scheduleDay = registerInfoMapper.selectScheduleRateByDay();
        // 金额统计
        List<LinkedHashMap<String, BigDecimal>> registerPriceDay = registerInfoMapper.selectRegisterPriceByDay();
        List<LinkedHashMap<String, BigDecimal>> drugPriceDay = registerInfoMapper.selectDrugPriceByDay();

        result.put("series", new LinkedHashMap<String, Object>() {
            {
                put("registerDay", registerDay);
                put("scheduleDay", scheduleDay);
            }
        });
        result.put("series1", new LinkedHashMap<String, Object>() {
            {
                put("registerPriceDay", registerPriceDay);
                put("drugPriceDay", drugPriceDay);
            }
        });
        return result;
    }

    /**
     * 根据科室获取医生排班信息
     *
     * @param officeId 科室ID
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectDoctorByOfficeId(Integer officeId) {
        // 返回数据
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        // 获取医生信息
        List<DoctorInfo> doctorInfoList = this.list(Wrappers.<DoctorInfo>lambdaQuery().eq(DoctorInfo::getOfficesId, officeId));
        List<Integer> doctorIds = doctorInfoList.stream().map(DoctorInfo::getId).collect(Collectors.toList());
        Map<Integer, DoctorInfo> doctorMap = doctorInfoList.stream().collect(Collectors.toMap(DoctorInfo::getId, e -> e));
        // 获取今天14天的排班信息
        List<ScheduleInfo> scheduleInfoList = scheduleInfoMapper.selectScheduleListByDay(doctorIds);
        // 根据医生ID分组
        Map<String, List<ScheduleInfo>> scheduleMap = scheduleInfoList.stream().collect(Collectors.groupingBy(ScheduleInfo::getStaffIds));

        scheduleMap.forEach((key, value) -> {
           result.add(new LinkedHashMap<String, Object>() {
               {
                   put("doctor", doctorMap.get(Integer.parseInt(key)));
                   put("schedule", value);
               }
           });
        });
        return result;
    }

    /**
     * 根据科室获取医生排班信息
     *
     * @param officeId 科室ID
     * @return 结果
     */
    @Override
    public List<ScheduleInfo> selectDoctorByOfficeIdFix(Integer officeId) {
        // 获取医生信息
        List<DoctorInfo> doctorInfoList = this.list(Wrappers.<DoctorInfo>lambdaQuery().eq(DoctorInfo::getOfficesId, officeId));
        List<Integer> doctorIds = doctorInfoList.stream().map(DoctorInfo::getId).collect(Collectors.toList());
        Map<Integer, DoctorInfo> doctorMap = doctorInfoList.stream().collect(Collectors.toMap(DoctorInfo::getId, e -> e));
        // 获取今天14天的排班信息
        return scheduleInfoMapper.selectScheduleListByDay(doctorIds);
    }
}
