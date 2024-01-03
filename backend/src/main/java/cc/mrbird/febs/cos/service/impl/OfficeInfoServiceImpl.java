package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.OfficeInfo;
import cc.mrbird.febs.cos.dao.OfficeInfoMapper;
import cc.mrbird.febs.cos.service.IOfficeInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class OfficeInfoServiceImpl extends ServiceImpl<OfficeInfoMapper, OfficeInfo> implements IOfficeInfoService {

    /**
     * 分页获取科室管理信息
     *
     * @param page       分页对象
     * @param officeInfo 科室管理信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOfficePage(Page<OfficeInfo> page, OfficeInfo officeInfo) {
        return baseMapper.selectOfficePage(page, officeInfo);
    }
}
