package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.DeptInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IDeptInfoService extends IService<DeptInfo> {

    /**
     * 分页获取科室信息
     *
     * @param page     分页对象
     * @param deptInfo 科室信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDeptPage(Page<DeptInfo> page, DeptInfo deptInfo);
}
