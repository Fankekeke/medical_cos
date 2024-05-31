package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DoctorInfo;
import cc.mrbird.febs.cos.entity.HospitalInfo;
import cc.mrbird.febs.cos.entity.RegisterInfo;
import cc.mrbird.febs.cos.dao.RegisterInfoMapper;
import cc.mrbird.febs.cos.service.IDoctorInfoService;
import cc.mrbird.febs.cos.service.IRegisterInfoService;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegisterInfoServiceImpl extends ServiceImpl<RegisterInfoMapper, RegisterInfo> implements IRegisterInfoService {

    private final IDoctorInfoService doctorInfoService;

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
}
