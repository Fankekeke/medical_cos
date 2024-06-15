package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.utils.SpaceUtil;
import cc.mrbird.febs.cos.entity.HospitalInfo;
import cc.mrbird.febs.cos.dao.HospitalInfoMapper;
import cc.mrbird.febs.cos.service.IHospitalInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
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
     * 逆地址解析
     */
    @Override
    public void addressResolve() {
        // 获取所有医院信息
        List<HospitalInfo> hospitalInfoList = this.list(Wrappers.<HospitalInfo>lambdaQuery().isNotNull(HospitalInfo::getHospitalAddress));
        if (CollectionUtil.isEmpty(hospitalInfoList)) {
            return;
        }
        // 待更新的医院数据
        List<HospitalInfo> toupList = new ArrayList<>();
        for (HospitalInfo hospitalInfo : hospitalInfoList) {
            String point = SpaceUtil.getCoordinate(hospitalInfo.getHospitalAddress());
            if (StrUtil.isEmpty(point)) {
                continue;
            }
            List<String> pointList = StrUtil.split(point, ',');
            hospitalInfo.setLongitude(new BigDecimal(pointList.get(0)));
            hospitalInfo.setLatitude(new BigDecimal(pointList.get(1)));
            toupList.add(hospitalInfo);
        }
        this.updateBatchById(toupList);
    }

    /**
     * 医院统计
     *
     * @param type 类型 1.地区统计 2.医院类型 3.医院等级
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectHospitalRate(Integer type) {
        // 返回数据
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();

        // 获取所有医院信息
        List<HospitalInfo> hospitalInfoList = this.list();
        if (CollectionUtil.isEmpty(hospitalInfoList)) {
            return Collections.emptyList();
        }

        Map<String, List<HospitalInfo>> mapRate;
        switch (type) {
            case 1:
                mapRate = hospitalInfoList.stream().filter(e -> StrUtil.isNotEmpty(e.getHospitalArea())).collect(Collectors.groupingBy(HospitalInfo::getHospitalArea));
                break;
            case 2:
                mapRate = hospitalInfoList.stream().filter(e -> StrUtil.isNotEmpty(e.getHospitalNature())).collect(Collectors.groupingBy(HospitalInfo::getHospitalNature));
                break;
            case 3:
                mapRate = hospitalInfoList.stream().filter(e -> StrUtil.isNotEmpty(e.getHospitalGrade())).collect(Collectors.groupingBy(HospitalInfo::getHospitalGrade));
                break;
            default:
                return Collections.emptyList();
        }
        mapRate.forEach((key, value) -> {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>(){
                {
                    put("data", value);
                    put("name", key);
                }
            };
            result.add(item);
        });

        return result;
    }
}
