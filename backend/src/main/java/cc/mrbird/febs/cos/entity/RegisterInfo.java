package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 挂号记录
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RegisterInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 预约时间
     */
    private String registerDate;

    /**
     * 所属科室
     */
    private Integer deptId;

    /**
     * 员工ID
     */
    private Integer staffId;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 状态（0.未支付 1.进行中 2.填写处方 3.待支付 4.已完成 5.已退号）
     */
    private String status;

    /**
     * 支付时间
     */
    private String payDate;

    /**
     * 病例附件
     */
    private String file;

    /**
     * 备注
     */
    private String remark;

    /**
     * 编号
     */
    private String code;

    /**
     * 药品价格
     */
    private BigDecimal drugPrice;

    /**
     * 用药参考
     */
    private String reference;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 班次ID
     */
    private Integer scheduleId;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 医院名称
     */
    @TableField(exist = false)
    private String hospitalName;

    /**
     * 科室名称
     */
    @TableField(exist = false)
    private String officesName;

    /**
     * 所属医院
     */
    @TableField(exist = false)
    private Integer hospitalId;

}
