package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.RegisterInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IRegisterInfoService extends IService<RegisterInfo> {

    /**
     * 分页获取挂号信息
     *
     * @param page         分页对象
     * @param registerInfo 挂号信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRegisterPage(Page<RegisterInfo> page, RegisterInfo registerInfo);
}
