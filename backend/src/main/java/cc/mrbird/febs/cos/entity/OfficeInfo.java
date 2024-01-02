package cc.mrbird.febs.cos.entity;

import java.io.Serializable;
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

    private String officesName;

    private String hospitalName;

    private String doctorNum;

    private String officesHonor;

    private String officesEquipment;

    private String officesAbout;

    /**
     * 诊疗范围
     */
    private String officesDiagnosisScope;


}
