package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.ChronicInfo;
import cc.mrbird.febs.cos.dao.ChronicInfoMapper;
import cc.mrbird.febs.cos.service.IChronicInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class ChronicInfoServiceImpl extends ServiceImpl<ChronicInfoMapper, ChronicInfo> implements IChronicInfoService {

    /**
     * 分页获取慢性病信息
     *
     * @param page        分页对象
     * @param chronicInfo 慢性病信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryChronicPage(Page<ChronicInfo> page, ChronicInfo chronicInfo) {
        return baseMapper.queryChronicPage(page, chronicInfo);
    }
}
