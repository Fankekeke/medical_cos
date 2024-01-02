package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DoctorInfo;
import cc.mrbird.febs.cos.dao.DoctorInfoMapper;
import cc.mrbird.febs.cos.service.IDoctorInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author FanK
 */
@Service
public class DoctorInfoServiceImpl extends ServiceImpl<DoctorInfoMapper, DoctorInfo> implements IDoctorInfoService {

}
