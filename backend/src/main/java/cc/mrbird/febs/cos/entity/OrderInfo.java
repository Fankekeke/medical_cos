package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
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
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    private String code;

    /**
     * 总价格
     */
    private BigDecimal totalCost;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 购买时间
     */
    private LocalDateTime createDate;

    /**
     * 订单状态（1.已下单 2.配送中 3.已收货）
     */
    private Integer orderStatus;

    /**
     * 订单详情
     */
    private String orderItem;


}
