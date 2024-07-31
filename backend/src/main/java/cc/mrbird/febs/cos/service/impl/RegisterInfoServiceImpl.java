package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.ScheduleInfoMapper;
import cc.mrbird.febs.cos.dao.UserInfoMapper;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.RegisterInfoMapper;
import cc.mrbird.febs.cos.service.IDoctorInfoService;
import cc.mrbird.febs.cos.service.IRegisterInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegisterInfoServiceImpl extends ServiceImpl<RegisterInfoMapper, RegisterInfo> implements IRegisterInfoService {

    private final IDoctorInfoService doctorInfoService;

    private final UserInfoMapper userInfoMapper;

    private final ScheduleInfoMapper scheduleInfoMapper;

    /**
     * 分页获取挂号记录信息
     *
     * @param page         分页对象
     * @param registerInfo 挂号记录信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectRegisterPage(Page<RegisterInfo> page, RegisterInfo registerInfo) {
        return baseMapper.selectRegisterPage(page, registerInfo);
    }

    /**
     * 获取挂号排名
     *
     * @param year  年份
     * @param month 月份
     * @return 结果
     */
    @Override
    public List<DoctorInfo> selectRegisterByDoctorRank(String year, String month) {
        // 获取医生信息
        List<DoctorInfo> doctorInfoList = doctorInfoService.list();
        if (CollectionUtil.isEmpty(doctorInfoList)) {
            return doctorInfoList;
        }
        // 获取本月挂号信息
        List<RegisterInfo> registerList = baseMapper.selectRegisterByDate(year, month);
        if (CollectionUtil.isNotEmpty(registerList)) {

        }
        return null;
    }

    /**
     * 添加挂号申请
     *
     * @param registerInfo 挂号信息
     * @return 结果
     */
    @Override
    public RegisterInfo registerOrderAdd(RegisterInfo registerInfo) {
        // 所属用户
        UserInfo user = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, registerInfo.getUserId()));
        if (user != null) {
            registerInfo.setUserId(user.getId());
        }

        // 价格50米
        registerInfo.setPrice(BigDecimal.valueOf(50));
        // 挂号班次信息
        ScheduleInfo schedule = scheduleInfoMapper.selectById(registerInfo.getScheduleId());
        // 预约时间
        registerInfo.setRegisterDate(schedule.getTaskDate());
        // 所属科室
        registerInfo.setDeptId(schedule.getOfficeId());
        // 医生信息
        DoctorInfo doctorInfo = doctorInfoService.getById(schedule.getStaffIds());
        if (doctorInfo != null) {
            // 所属医院
            registerInfo.setHospitalId(doctorInfo.getHospitalId());
        }
        // 员工ID
        registerInfo.setStaffId(Integer.valueOf(schedule.getStaffIds()));
        // 开始时间
        registerInfo.setStartDate(schedule.getStartDate());
        // 结束时间
        registerInfo.setEndDate(schedule.getEndDate());
        // 状态
        registerInfo.setStatus("0");
        // 挂号编号
        registerInfo.setCode("REG-" + System.currentTimeMillis());
        registerInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        registerInfo.setScheduleId(schedule.getId());
        this.save(registerInfo);
        return registerInfo;
    }
}
