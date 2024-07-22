package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.RegisterInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface RegisterInfoMapper extends BaseMapper<RegisterInfo> {

    /**
     * 分页获取挂号记录信息
     *
     * @param page         分页对象
     * @param registerInfo 挂号记录信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRegisterPage(Page<RegisterInfo> page, @Param("registerInfo") RegisterInfo registerInfo);

    /**
     * 获取挂号记录
     *
     * @param registerIds 挂号ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectRegisterList(@Param("registerIds") List<Integer> registerIds);

    /**
     * 根据时间获取挂号信息
     *
     * @param year  年份
     * @param month 月份
     * @return 结果
     */
    List<RegisterInfo> selectRegisterByDate(@Param("year") String year, @Param("month") String month);

    /**
     * 获取本月订单信息
     *
     * @return 结果
     */
    List<RegisterInfo> selectRegisterByMonth(@Param("hospitalId") Integer hospitalId);

    /**
     * 获取本年订单信息
     *
     * @return 结果
     */
    List<RegisterInfo> selectRegisterByYear(@Param("hospitalId") Integer hospitalId);

    /**
     * 根据医生获取挂号信息
     *
     * @param doctorId 医生ID
     * @param year     年度
     * @param month    月度
     * @return 结果
     */
    List<RegisterInfo> selectRegisterByMonthDoctor(@Param("doctorId") Integer doctorId, @Param("year") Integer year, @Param("month") Integer month);

    /**
     * 挂号数量统计
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectRegisterRateByDay();

    /**
     * 排班数量统计
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectScheduleRateByDay();

    /**
     * 挂号金额统计
     *
     * @return 结果
     */
    List<LinkedHashMap<String, BigDecimal>> selectRegisterPriceByDay();

    /**
     * 处方金额统计
     *
     * @return 结果
     */
    List<LinkedHashMap<String, BigDecimal>> selectDrugPriceByDay();
}
