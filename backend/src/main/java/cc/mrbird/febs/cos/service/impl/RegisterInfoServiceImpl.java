package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.RegisterInfo;
import cc.mrbird.febs.cos.dao.RegisterInfoMapper;
import cc.mrbird.febs.cos.service.IRegisterInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author FanK
 */
@Service
public class RegisterInfoServiceImpl extends ServiceImpl<RegisterInfoMapper, RegisterInfo> implements IRegisterInfoService {

}
