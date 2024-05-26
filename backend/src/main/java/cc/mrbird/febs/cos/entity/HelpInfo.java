package cc.mrbird.febs.cos.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 问题求助
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class HelpInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 问题的类型
     */
    private String questionType;

    /**
     * 问题详情
     */
    private String question;

    /**
     * 回答
     */
    private String answer;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 状态（0.未处理 1.已处理）
     */
    private String status;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 处理时间
     */
    private String replyDate;

    @TableField(exist = false)
    private String userName;
}
