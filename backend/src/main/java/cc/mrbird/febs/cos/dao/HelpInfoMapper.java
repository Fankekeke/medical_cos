package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.HelpInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface HelpInfoMapper extends BaseMapper<HelpInfo> {

    /**
     * 分页获取问题求助信息
     *
     * @param page     分页对象
     * @param helpInfo 问题求助信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectHelpPage(Page<HelpInfo> page, @Param("helpInfo") HelpInfo helpInfo);
}
