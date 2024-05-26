package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.InventoryStatistics;
import cc.mrbird.febs.cos.service.IInventoryStatisticsService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/inventory-statistics")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InventoryStatisticsController {

    private final IInventoryStatisticsService inventoryStatisticsService;

    /**
     * 分页获取库存统计信息
     *
     * @param page                分页对象
     * @param inventoryStatistics 库存统计信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<InventoryStatistics> page, InventoryStatistics inventoryStatistics) {
        return R.ok(inventoryStatisticsService.selectInventoryStatisticsPage(page, inventoryStatistics));
    }

    /**
     * 查询库存统计信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(inventoryStatisticsService.getById(id));
    }

    /**
     * 查询库存统计信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(inventoryStatisticsService.list());
    }

    /**
     * 新增库存统计信息
     *
     * @param inventoryStatistics 库存统计信息
     * @return 结果
     */
    @PostMapping
    public R save(InventoryStatistics inventoryStatistics) {
        return R.ok(inventoryStatisticsService.save(inventoryStatistics));
    }

    /**
     * 修改库存统计信息
     *
     * @param inventoryStatistics 库存统计信息
     * @return 结果
     */
    @PutMapping
    public R edit(InventoryStatistics inventoryStatistics) {
        return R.ok(inventoryStatisticsService.updateById(inventoryStatistics));
    }

    /**
     * 删除库存统计信息
     *
     * @param ids ids
     * @return 库存统计信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(inventoryStatisticsService.removeByIds(ids));
    }
}
