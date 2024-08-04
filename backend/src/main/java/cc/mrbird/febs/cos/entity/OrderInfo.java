package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

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
     * 员工ID
     */
    private Integer staffId;

    /**
     * 物流ID
     */
    private Integer logisticsId;

    /**
     * 购买时间
     */
    private String createDate;

    /**
     * 订单状态（0.未付款 1.已下单 2.配送中 3.已收货）
     */
    private Integer orderStatus;

    /**
     * 订单详情
     */
    private String orderItem;

    /**
     * 所属医院
     */
    private Integer pharmacyId;

    /**
     * 挂号ID
     */
    private Integer registerId;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String pharmacyName;

    /**
     * 所属医院
     */
    @TableField(exist = false)
    private Integer hospitalId;

    /**
     * 所属医生
     */
    @TableField(exist = false)
    private Integer doctorId;

}
