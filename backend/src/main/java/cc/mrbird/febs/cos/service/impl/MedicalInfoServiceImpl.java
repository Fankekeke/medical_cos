package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.MedicalInfo;
import cc.mrbird.febs.cos.dao.MedicalInfoMapper;
import cc.mrbird.febs.cos.service.IMedicalInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class MedicalInfoServiceImpl extends ServiceImpl<MedicalInfoMapper, MedicalInfo> implements IMedicalInfoService {

    /**
     * 分页获取医疗资讯信息
     *
     * @param page        分页对象
     * @param medicalInfo 医疗资讯信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectMedicalPage(Page<MedicalInfo> page, MedicalInfo medicalInfo) {
        return baseMapper.selectMedicalPage(page, medicalInfo);
    }
}
