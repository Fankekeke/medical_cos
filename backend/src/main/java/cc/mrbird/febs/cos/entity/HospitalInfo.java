package cc.mrbird.febs.cos.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 医院管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class HospitalInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 医院编号
     */
    private String code;

    /**
     * 账户ID
     */
    private Integer userId;

    /**
     * 医院地区
     */
    private String hospitalArea;

    /**
     * 医院图片
     */
    private String hospitalImg;

    /**
     * 医院院长姓名
     */
    private String hospitalDeanName;

    /**
     * 医院建院年份
     */
    private String hospitalYear;

    /**
     * 医院类型
     */
    private String hospitalNature;

    /**
     * 医院等级
     */
    private String hospitalGrade;

    /**
     * 医院科室数量
     */
    private Integer hospitalOfficesNum;

    /**
     * 医保人数（保留）
     */
    private Integer medicalInsuranceNum;

    /**
     * 病床数量
     */
    private Integer hospitalBedNum;

    /**
     * 年门诊量
     */
    private Integer outpatientNum;

    /**
     * 是否医保
     */
    private String isMedicalInsurance;

    /**
     * 医院设备介绍
     */
    private String hospitalEquipment;

    /**
     * 医院简介
     */
    private String hospitalAbout;

    /**
     * 医院荣誉
     */
    private String hospitalHonor;

    /**
     * 医院网址
     */
    private String hospitalUrl;

    /**
     * 医院电话
     */
    private String hospitalPhone;

    /**
     * 医院地址
     */
    private String hospitalAddress;

    /**
     * 医院邮编
     */
    private String hospitalPostCode;

    /**
     * 医院公交路线
     */
    private String hospitalBusRoute;

    /**
     * 是否开启挂号
     */
    private Integer isOpen;

    /**
     * 图片
     */
    private String images;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 营业额
     */
    @TableField(exist = false)
    private BigDecimal salesTotal;

    /**
     * 单数
     */
    @TableField(exist = false)
    private Integer salesNum;
}
