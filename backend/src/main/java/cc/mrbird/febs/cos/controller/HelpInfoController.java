package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.HelpInfo;
import cc.mrbird.febs.cos.service.IHelpInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/help-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HelpInfoController {

    private final IHelpInfoService helpInfoService;

    /**
     * 分页获取问题求助信息
     *
     * @param page     分页对象
     * @param helpInfo 问题求助信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<HelpInfo> page, HelpInfo helpInfo) {
        return R.ok();
    }

    /**
     * 获取详情信息
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(helpInfoService.getById(id));
    }

    /**
     * 获取信息列表
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
