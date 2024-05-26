package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DoctorInfo;
import cc.mrbird.febs.cos.dao.DoctorInfoMapper;
import cc.mrbird.febs.cos.service.IDoctorInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class DoctorInfoServiceImpl extends ServiceImpl<DoctorInfoMapper, DoctorInfo> implements IDoctorInfoService {

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
}
