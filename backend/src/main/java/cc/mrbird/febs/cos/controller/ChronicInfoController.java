package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.ChronicInfo;
import cc.mrbird.febs.cos.service.IChronicInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
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
@RequestMapping("/cos/chronic-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChronicInfoController {

    private final IChronicInfoService chronicInfoService;

    /**
     * 分页获取慢性病信息
     *
     * @param page        分页对象
     * @param chronicInfo 慢性病信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<ChronicInfo> page, ChronicInfo chronicInfo) {
        return R.ok(chronicInfoService.queryChronicPage(page, chronicInfo));
    }

    /**
     * 查询慢性病信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(chronicInfoService.getById(id));
    }

    /**
     * 查询慢性病信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(chronicInfoService.list());
    }

    /**
     * 新增慢性病信息
     *
     * @param chronicInfo 慢性病信息
     * @return 结果
     */
    @PostMapping
    public R save(ChronicInfo chronicInfo) {
        chronicInfo.setCode("CHR-" + System.currentTimeMillis());
        chronicInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(chronicInfoService.save(chronicInfo));
    }

    /**
     * 修改慢性病信息
     *
     * @param chronicInfo 慢性病信息
     * @return 结果
     */
    @PutMapping
    public R edit(ChronicInfo chronicInfo) {
        return R.ok(chronicInfoService.updateById(chronicInfo));
    }

    /**
     * 删除慢性病信息
     *
     * @param ids ids
     * @return 慢性病信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(chronicInfoService.removeByIds(ids));
    }
}
