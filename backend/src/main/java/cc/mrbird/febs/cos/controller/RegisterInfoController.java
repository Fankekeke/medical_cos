package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.RegisterInfo;
import cc.mrbird.febs.cos.service.IRegisterInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/register-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegisterInfoController {

    private final IRegisterInfoService registerInfoService;

    /**
     * 分页获取挂号信息
     *
     * @param page         分页对象
     * @param registerInfo 挂号信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<RegisterInfo> page, RegisterInfo registerInfo) {
        return R.ok(registerInfoService.selectRegisterPage(page, registerInfo));
    }

    /**
     * 获取详情信息
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(registerInfoService.getById(id));
    }

    /**
     * 获取信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(registerInfoService.list());
    }

    /**
     * 新增挂号信息
     *
     * @param registerInfo 挂号信息
     * @return 结果
     */
    @PostMapping
    public R save(RegisterInfo registerInfo) {
        return R.ok(registerInfoService.save(registerInfo));
    }

    /**
     * 修改挂号信息
     *
     * @param registerInfo 挂号信息
     * @return 结果
     */
    @PutMapping
    public R edit(RegisterInfo registerInfo) {
        return R.ok(registerInfoService.updateById(registerInfo));
    }

    /**
     * 删除挂号信息
     *
     * @param ids ids
     * @return 挂号信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(registerInfoService.removeByIds(ids));
    }
}
