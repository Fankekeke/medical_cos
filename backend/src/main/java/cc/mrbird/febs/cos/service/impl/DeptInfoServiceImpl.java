package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DeptInfo;
import cc.mrbird.febs.cos.dao.DeptInfoMapper;
import cc.mrbird.febs.cos.service.IDeptInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class DeptInfoServiceImpl extends ServiceImpl<DeptInfoMapper, DeptInfo> implements IDeptInfoService {

    /**
     * 分页获取科室信息
     *
     * @param page     分页对象
     * @param deptInfo 科室信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectDeptPage(Page<DeptInfo> page, DeptInfo deptInfo) {
        return baseMapper.selectDeptPage(page, deptInfo);
    }
}
