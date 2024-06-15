package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.HospitalInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author FanK
 */
public interface IHospitalInfoService extends IService<HospitalInfo> {

    /**
     * 分页获取医院信息
     *
     * @param page     分页对象
     * @param hospitalInfo 医院信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectHospitalPage(Page<HospitalInfo> page, HospitalInfo hospitalInfo);

    /**
     * 医院统计
     *
     * @param type 1.按区域统计 2.按医院等级统计 3.按医院类型统计
     * @return 结果
     */
    Map<String, List<HospitalInfo>> selectStatisticsByType(Integer type);

    /**
     * 查询医院地图
     *
     * @param key 关键字
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectHospitalByMap(String key);

    /**
     * 逆地址解析
     */
    void addressResolve();

    /**
     * 医院统计
     *
     * @param type 类型 1.地区统计 2.医院类型 3.医院等级
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectHospitalRate(Integer type);
}
