package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.HospitalInfo;
import cc.mrbird.febs.cos.dao.HospitalInfoMapper;
import cc.mrbird.febs.cos.service.IHospitalInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

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
}
