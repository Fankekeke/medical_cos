package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.OfficeInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface OfficeInfoMapper extends BaseMapper<OfficeInfo> {

    /**
     * 分页获取科室信息
     *
     * @param page       分页对象
     * @param officeInfo 科室信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOffocePage(Page<OfficeInfo> page, @Param("officeInfo") OfficeInfo officeInfo);
}
