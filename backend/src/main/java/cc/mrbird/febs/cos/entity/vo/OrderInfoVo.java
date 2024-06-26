package cc.mrbird.febs.cos.entity.vo;

import cc.mrbird.febs.cos.entity.OrderInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInfoVo extends OrderInfo {

    private String orderDetailList;

}
