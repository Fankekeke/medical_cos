package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.ChronicInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface ChronicInfoMapper extends BaseMapper<ChronicInfo> {

    /**
     * 分页获取慢性病信息
     *
     * @param page        分页对象
     * @param chronicInfo 慢性病信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryChronicPage(Page<ChronicInfo> page, @Param("chronicInfo") ChronicInfo chronicInfo);
}
