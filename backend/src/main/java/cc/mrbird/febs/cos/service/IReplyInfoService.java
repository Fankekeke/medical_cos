package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.ReplyInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IReplyInfoService extends IService<ReplyInfo> {

    /**
     * 分页获取评论信息
     *
     * @param page      分页对象
     * @param replyInfo 评论信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectReplyPage(Page<ReplyInfo> page, ReplyInfo replyInfo);
}
