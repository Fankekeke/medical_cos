package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.OfficeInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IOfficeInfoService extends IService<OfficeInfo> {

    /**
     * 分页获取科室信息
     *
     * @param page       分页对象
     * @param officeInfo 科室信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOffocePage(Page<OfficeInfo> page, OfficeInfo officeInfo);
}
