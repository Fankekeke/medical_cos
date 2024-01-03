package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.HelpInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IHelpInfoService extends IService<HelpInfo> {

    /**
     * 分页获取问题求助信息
     *
     * @param page     分页对象
     * @param helpInfo 问题求助信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectHelpPage(Page<HelpInfo> page, HelpInfo helpInfo);
}
