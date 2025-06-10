package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 慢性病管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ChronicInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 病例编号
     */
    private String code;

    /**
     * 病例名称
     */
    private String name;

    /**
     * 父级病例
     */
    private String parentName;

    /**
     * 病例内容
     */
    private String content;

    /**
     * 创建时间
     */
    private String createDate;
    private String userName;
    private String sex;
    private String birthdayDate;
    private String idCard;
    private String phone;
    private String hypertensionFlag;
    private String diabetesFlag;
    private String mentalDisordersFlag;
    private String copdFlag;
    private String serivceDept;
    private String serviceDoctor;
    private String address;
    private BigDecimal fastingBloodSugar;
    private BigDecimal postprandialBloodSuga;
    private BigDecimal bloodPressure;


}
