package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 会员编号
     */
    private String code;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 邮箱地址
     */
    private String mail;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 省份
     */
    private String province;

    /**
     * 市区
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 所属账户
     */
    private Long userId;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 身高
     */
    private Integer height;

    /**
     * 体重
     */
    private BigDecimal weight;

    /**
     * 性别（1.男 2.女）
     */
    private String sex;

    /**
     * 出生日期
     */
    private String birthday;

    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 头像
     */
    private String images;


}
