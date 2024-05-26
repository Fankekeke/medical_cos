package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.RegisterInfo;
import cc.mrbird.febs.cos.dao.RegisterInfoMapper;
import cc.mrbird.febs.cos.service.IRegisterInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class RegisterInfoServiceImpl extends ServiceImpl<RegisterInfoMapper, RegisterInfo> implements IRegisterInfoService {


    /**
     * 分页获取挂号记录信息
     *
     * @param page         分页对象
     * @param registerInfo 挂号记录信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectRegisterPage(Page<RegisterInfo> page, RegisterInfo registerInfo) {
        return baseMapper.selectRegisterPage(page, registerInfo);
    }
}
