package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.DoctorInfo;
import cc.mrbird.febs.cos.entity.HospitalInfo;
import cc.mrbird.febs.cos.entity.RegisterInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IRegisterInfoService extends IService<RegisterInfo> {

    /**
     * 分页获取挂号记录信息
     *
     * @param page         分页对象
     * @param registerInfo 挂号记录信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRegisterPage(Page<RegisterInfo> page, RegisterInfo registerInfo);

    /**
     * 获取挂号排名
     *
     * @param year  年份
     * @param month 月份
     * @return 结果
     */
    List<DoctorInfo> selectRegisterByDoctorRank(String year, String month);

    /**
     * 添加挂号申请
     *
     * @param registerInfo 挂号信息
     * @return 结果
     */
    RegisterInfo registerOrderAdd(RegisterInfo registerInfo);
}
