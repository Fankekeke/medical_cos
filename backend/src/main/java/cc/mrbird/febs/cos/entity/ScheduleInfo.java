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
     * 所属医院
     */
    @TableField(exist = false)
    private Integer hospitalId;
}
