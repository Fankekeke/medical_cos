package cc.mrbird.febs.cos.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 科室管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OfficeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 科室名称
     */
    private String officesName;

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 所属医院
     */
    private Integer hospitalId;

    /**
     * 医生人数
     */
    private String doctorNum;

    /**
     * 荣誉资质
     */
    private String officesHonor;

    /**
     * 设备
     */
    private String officesEquipment;

    /**
     * 关于我们
     */
    private String officesAbout;

    /**
     * 诊疗范围
     */
    private String officesDiagnosisScope;

}
