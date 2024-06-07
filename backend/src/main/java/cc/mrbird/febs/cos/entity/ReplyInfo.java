package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 评论管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ReplyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 讯息ID
     */
    private Integer medicalId;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String name;

    /**
     * 标题
     */
    @TableField(exist = false)
    private String title;
}
