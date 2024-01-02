package cc.mrbird.febs.cos.entity;

import java.io.Serializable;
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

    private String doctorName;

    private String doctorSex;

    /**
     * 所属医院
     */
    private String hospitalName;

    /**
     * 所属科室
     */
    private String officesName;

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

    private String doctorAbout;


}
