package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class DeptInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 科室编号
     */
    private String code;

    /**
     * 科室名称
     */
    private String name;

    /**
     * 备注
     */
    private String content;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 图片
     */
    private String images;

    /**
     * 状态（0.废弃 1.正常）
     */
    private String status;


}
