package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.ReplyInfo;
import cc.mrbird.febs.cos.service.IReplyInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/reply-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReplyInfoController {

    private final IReplyInfoService replyInfoService;

    /**
     * 分页获取评论信息
     *
     * @param page      分页对象
     * @param replyInfo 评论信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<ReplyInfo> page, ReplyInfo replyInfo) {
        return R.ok(replyInfoService.selectReplyPage(page, replyInfo));
    }

    /**
     * 查询评论信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(replyInfoService.getById(id));
    }

    /**
     * 查询评论信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(replyInfoService.list());
    }

    /**
     * 新增评论信息
     *
     * @param replyInfo 评论信息
     * @return 结果
     */
    @PostMapping
    public R save(ReplyInfo replyInfo) {
        return R.ok(replyInfoService.save(replyInfo));
    }

    /**
     * 修改评论信息
     *
     * @param replyInfo 评论信息
     * @return 结果
     */
    @PutMapping
    public R edit(ReplyInfo replyInfo) {
        return R.ok(replyInfoService.updateById(replyInfo));
    }

    /**
     * 删除评论信息
     *
     * @param ids ids
     * @return 评论信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(replyInfoService.removeByIds(ids));
    }
}
