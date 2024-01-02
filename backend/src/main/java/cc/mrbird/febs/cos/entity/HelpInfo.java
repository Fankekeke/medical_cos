package cc.mrbird.febs.cos.entity;

import java.io.Serializable;
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
     * 问题的类型
     */
    private String questionType;

    private String question;

    private String answer;


}
