package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.HospitalInfo;
import cc.mrbird.febs.cos.dao.HospitalInfoMapper;
import cc.mrbird.febs.cos.service.IHospitalInfoService;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
public class HospitalInfoServiceImpl extends ServiceImpl<HospitalInfoMapper, HospitalInfo> implements IHospitalInfoService {

    /**
     * 分页获取医院信息
     *
     * @param page         分页对象
     * @param hospitalInfo 医院信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectHospitalPage(Page<HospitalInfo> page, HospitalInfo hospitalInfo) {
        return baseMapper.selectHospitalPage(page, hospitalInfo);
    }

    /**
     * 医院统计
     *
     * @param type 1.按区域统计 2.按医院等级统计 3.按医院类型统计
     * @return 结果
     */
    @Override
    public Map<String, List<HospitalInfo>> selectStatisticsByType(Integer type) {
        // 获取所有医院信息
        List<HospitalInfo> hospitalInfoList = this.list();
        if (CollectionUtil.isEmpty(hospitalInfoList)) {
            return null;
        }
        switch (type) {
            case 1:
                // 按区域统计
                return hospitalInfoList.stream().collect(Collectors.groupingBy(HospitalInfo::getHospitalArea));
            case 2:
                // 按医院等级统计
                return hospitalInfoList.stream().collect(Collectors.groupingBy(HospitalInfo::getHospitalGrade));
            case 3:
                // 按医院类型统计
                return hospitalInfoList.stream().collect(Collectors.groupingBy(HospitalInfo::getHospitalNature));
            default:
        }
        return null;
    }

    /**
     * 查询医院地图
     *
     * @param key 关键字
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectHospitalByMap(String key) {
        // 获取医院信息【过滤经纬度为空】
        return baseMapper.selectHospitalByMap(key);
    }

    /**
     * 医院统计
     *
     * @param type 类型 1.地区统计 2.医院类型 3.医院等级
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectHospitalRate(Integer type) {
        return null;
    }
}
