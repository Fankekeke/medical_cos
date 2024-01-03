package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.HospitalInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IHospitalInfoService extends IService<HospitalInfo> {

    /**
     * 分页获取医院信息
     *
     * @param page         分页对象
     * @param hospitalInfo 医院信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectHospitalPage(Page<HospitalInfo> page, HospitalInfo hospitalInfo);
}
