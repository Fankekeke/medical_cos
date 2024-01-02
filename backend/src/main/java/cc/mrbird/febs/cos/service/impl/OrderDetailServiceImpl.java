package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.OrderDetail;
import cc.mrbird.febs.cos.dao.OrderDetailMapper;
import cc.mrbird.febs.cos.service.IOrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author FanK
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

}
