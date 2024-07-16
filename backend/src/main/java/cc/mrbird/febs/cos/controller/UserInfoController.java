package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.service.CacheService;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/user-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserInfoController {

    private final IUserInfoService userInfoService;

    private final UserService userService;

    private final CacheService cacheService;

    /**
     * 分页获取员工信息
     *
     * @param page     分页对象
     * @param userInfo 员工信息
     * @return 结果
     */
    @GetMapping("/page/list")
    public R page(Page<UserInfo> page, UserInfo userInfo) {
        return R.ok(userInfoService.selectUserPage(page, userInfo));
    }

    /**
     * 根据用户ID获取统计数据
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/selectRateByUserId/{userId}")
    public R selectRateByUserId(@PathVariable("userId") Integer userId) {
        return R.ok(userInfoService.selectRateByUserId(userId));
    }

    /**
     * 用户状态更改
     *
     * @param flag   状态
     * @param userId 用户ID
     * @return 结果
     */
    @PostMapping("/user/audit")
    public R userStatusAudit(Integer flag, Integer userId) throws Exception {
        UserInfo userInfo = userInfoService.getById(userId);
        userInfo.setStatus(flag.toString());
        userInfoService.updateById(userInfo);

        userService.update(Wrappers.<User>lambdaUpdate().set(User::getStatus, flag).eq(User::getUserId, userInfo.getUserId()));
        User user = userService.getById(userId);
        // 重新将用户信息，用户角色信息，用户权限信息 加载到 redis中
        cacheService.saveUser(user.getUsername());
        cacheService.saveRoles(user.getUsername());
        cacheService.savePermissions(user.getUsername());
        return R.ok(true);
    }

    /**
     * 查询员工信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(userInfoService.getById(id));
    }

    /**
     * 查询员工信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(userInfoService.list());
    }

    /**
     * 新增员工信息
     *
     * @param userInfo 员工信息
     * @return 结果
     */
    @PostMapping
    public R save(UserInfo userInfo) {
        return R.ok(userInfoService.save(userInfo));
    }

    /**
     * 修改员工信息
     *
     * @param userInfo 员工信息
     * @return 结果
     */
    @PutMapping
    public R edit(UserInfo userInfo) {
        return R.ok(userInfoService.updateById(userInfo));
    }

    /**
     * 删除员工信息
     *
     * @param ids ids
     * @return 员工信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(userInfoService.removeByIds(ids));
    }
}
