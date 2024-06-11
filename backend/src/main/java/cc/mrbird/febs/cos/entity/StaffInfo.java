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
 * 员工管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StaffInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 员工编号
     */
    private String code;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 性别（1.男 2.女）
     */
    private Integer sex;

    /**
     * 状态（1.在职 2.离职 3.上线）
     */
    private Integer status;

    /**
     * 所属部门
     */
    private Integer deptId;

    /**
     * 照片
     */
    private String images;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 所属账户
     */
    private Long userId;

    /**
     * 邮箱地址
     */
    private String mail;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 备注
     */
    private String content;

    /**
     * 类型（1.主治医生 2.护士 3.配药员）
     */
    private String type;

    /**
     * 是否为店长（0.否 1.是）
     */
    private Integer isAdmin;

    /**
     * 离职时间
     */
    private String resignDate;

    /**
     * 工资
     */
    private BigDecimal salary;

    /**
     * 职位（1.店长 2.药师 3.普通员工）
     */
    private String position;

    /**
     * 医院名称
     */
    @TableField(exist = false)
    private String hospitalName;

    /**
     * 所属医院
     */
    @TableField(exist = false)
    private Integer hospitalId;

}
