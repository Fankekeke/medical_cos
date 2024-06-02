package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.PharmacyInventory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IPharmacyInventoryService extends IService<PharmacyInventory> {

    /**
     * 分页获取医院库存信息
     *
     * @param page     分页对象
     * @param pharmacyInventory 医院库存信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectPharmacyInventoryPage(Page<PharmacyInventory> page, PharmacyInventory pharmacyInventory);

    /**
     * 获取药品信息
     *
     * @param key key
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectPharmacyDrugList(String key);

    /**
     * 批量设置库房库存
     * @param pharmacyId 参数
     * @param pharmacyInventoryList 参数
     * @return 结果
     * @throws Exception 异常
     */
    boolean batchPutInventory(Integer pharmacyId, String pharmacyInventoryList) throws Exception;

    /**
     * 根据医院ID获取库存信息
     *
     * @param pharmacyId 医院ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectInventoryByPharmacy(Integer pharmacyId);

    /**
     * 设置库存
     *
     * @param pharmacyInventory 医院库存信息
     * @return 结果
     */
    boolean putInventory(PharmacyInventory pharmacyInventory);

    /**
     * 设置库存
     *
     * @param pharmacyInventory 医院库存信息
     * @return 结果
     */
    boolean outInventory(PharmacyInventory pharmacyInventory);
}
