package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.HelpInfo;
import cc.mrbird.febs.cos.dao.HelpInfoMapper;
import cc.mrbird.febs.cos.service.IHelpInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class HelpInfoServiceImpl extends ServiceImpl<HelpInfoMapper, HelpInfo> implements IHelpInfoService {

    /**
     * 分页获取问题求助信息
     *
     * @param page     分页对象
     * @param helpInfo 问题求助信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectHelpPage(Page<HelpInfo> page, HelpInfo helpInfo) {
        return baseMapper.selectHelpPage(page, helpInfo);
    }
}
