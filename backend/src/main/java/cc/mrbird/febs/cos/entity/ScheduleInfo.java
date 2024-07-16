package cc.mrbird.febs.cos.entity;

import java.time.LocalTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 排班信息
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ScheduleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 编号
     */
    private String code;

    /**
     * 排版名称
     */
    private String name;

    /**
     * 日期
     */
    private String taskDate;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 所属员工
     */
    private String staffIds;

    /**
     * 所属科室
     */
    private Integer officeId;

    /**
     * 是否为模板 0.否 1.是
     */
    private String status;

    /**
     * 所属医院
     */
    @TableField(exist = false)
    private Integer hospitalId;

    /**
     * 所属医生
     */
    @TableField(exist = false)
    private Integer staffId;

    /**
     * 医生名称
     */
    @TableField(exist = false)
    private String doctorName;

    /**
     * 照片
     */
    @TableField(exist = false)
    private String images;

    /**
     * 挂号ID
     */
    @TableField(exist = false)
    private Integer registerId;
}
