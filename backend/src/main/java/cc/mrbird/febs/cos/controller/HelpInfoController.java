package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.HelpInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IHelpInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/help-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HelpInfoController {

    private final IHelpInfoService helpInfoService;

    private final IUserInfoService userInfoService;

    /**
     * 分页获取问题求助信息
     *
     * @param page     分页对象
     * @param helpInfo 问题求助信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<HelpInfo> page, HelpInfo helpInfo) {
        return R.ok(helpInfoService.selectHelpPage(page, helpInfo));
    }

    /**
     * 查询问题求助信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(helpInfoService.getById(id));
    }

    /**
     * 查询问题求助信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(helpInfoService.list());
    }

    /**
     * 新增问题求助信息
     *
     * @param helpInfo 问题求助信息
     * @return 结果
     */
    @PostMapping
    public R save(HelpInfo helpInfo) {
        // 获取用户ID
        if (helpInfo.getUserId() != null) {
            UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, helpInfo.getUserId()));
            if (userInfo != null) {
                helpInfo.setUserId(userInfo.getId());
            }
        }
        return R.ok(helpInfoService.save(helpInfo));
    }

    /**
     * 修改问题求助信息
     *
     * @param helpInfo 问题求助信息
     * @return 结果
     */
    @PutMapping
    public R edit(HelpInfo helpInfo) {
        helpInfo.setStatus("1");
        helpInfo.setReplyDate(DateUtil.formatDateTime(new Date()));
        return R.ok(helpInfoService.updateById(helpInfo));
    }

    /**
     * 删除问题求助信息
     *
     * @param ids ids
     * @return 问题求助信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(helpInfoService.removeByIds(ids));
    }

}
