package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.ChronicInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IChronicInfoService extends IService<ChronicInfo> {

    /**
     * 分页获取慢性病信息
     *
     * @param page        分页对象
     * @param chronicInfo 慢性病信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryChronicPage(Page<ChronicInfo> page, ChronicInfo chronicInfo);
}
