package cc.mrbird.febs.cos.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 医生管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DoctorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 医生编号
     */
    private String code;

    /**
     * 账户ID
     */
    private Integer userId;

    /**
     * 性别
     */
    private String doctorSex;

    /**
     * 所属医院
     */
    private Integer hospitalId;

    /**
     * 所属医院
     */
    private String hospitalName;

    /**
     * 所属科室
     */
    private String officesName;

    /**
     * 所属科室
     */
    private Integer officesId;

    /**
     * 医生照片
     */
    private String doctorImg;

    /**
     * 医生职称
     */
    private String doctorTitle;

    /**
     * 教学支职称
     */
    private String teachTitle;

    /**
     * 行政职位
     */
    private String doctorAdministrative;

    /**
     * 学位
     */
    private String doctorDegree;

    /**
     * 医生特长
     */
    private String doctorForte;

    /**
     * 信息
     */
    private String doctorAbout;

    /**
     * 照片
     */
    private String images;
}
