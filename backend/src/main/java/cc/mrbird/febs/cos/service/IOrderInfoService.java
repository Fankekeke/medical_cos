package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.entity.HospitalInfo;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.vo.OrderDetailVo;
import cc.mrbird.febs.cos.entity.vo.OrderInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IOrderInfoService extends IService<OrderInfo> {
    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<OrderInfo> page, OrderInfo orderInfo);

    /**
     * 获取医院销售排名
     *
     * @param year  年份
     * @param month 月份
     * @return 结果
     */
    List<HospitalInfo> selectSaleRank(String year, String month);

    /**
     * 添加订单信息
     *
     * @param orderInfoVo 订单信息
     * @return 结果
     */
    boolean orderAdd(OrderInfoVo orderInfoVo, boolean flag);

    /**
     * 根据用户月份获取绩效
     *
     * @param staffCode 员工编号
     * @param year      年份
     * @param month     月份
     * @return 结果
     */
    LinkedHashMap<String, BigDecimal> selectPerformanceByStaffCodeMonth(String staffCode, String year, String month);

    /**
     * 主页数据
     *
     * @return 结果
     */
    LinkedHashMap<String, Object> homeData();

    /**
     * 主页数据
     *
     * @return 结果
     */
    LinkedHashMap<String, Object> homeDataHospital(Integer hospitalId);

    /**
     * 医生数据统计
     *
     * @param doctorId 医生ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectHomeDataByDoctor(Integer doctorId);

    /**
     * 订单付款
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    boolean orderPayment(String orderCode);

    /**
     * 订单打印小票
     *
     * @param orderId 订单ID
     * @return 结果
     */
    LinkedHashMap<String, Object> receipt(Integer orderId);

    /**
     * 订单发货
     *
     * @param orderId 订单ID
     * @param remark  物流信息
     * @return 结果
     */
    boolean orderShip(Integer orderId, String remark);

    /**
     * 订单付款
     *
     * @param orderCode 订单编号
     * @param staffCode 员工编号
     */
    void orderPaymentPlatform(String orderCode, String staffCode);

    /**
     * 线上订单付款
     *
     * @param orderCode 订单编号
     * @param staffCode 员工编号
     */
    void orderPaymentOnline(String orderCode, String staffCode);

    /**
     * 获取用户订单统计
     *
     * @param userId 用户ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectOrderRateByUser(Integer userId);

    /**
     * 用户提交订单
     *
     * @param orderDetailVo 订单信息
     * @return 结果
     */
    boolean orderSubmit(OrderDetailVo orderDetailVo) throws FebsException;

    /**
     * 设置数据
     *
     * @param type 类型
     */
    void setData(String type);

    /**
     * 根据医院获取销售统计
     *
     * @param hospitalId 医院ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectOrderRateByHospital(Integer hospitalId);

    /**
     * 获取药品销售统计
     *
     * @param date 统计日期
     * @return 结果
     */
    LinkedHashMap<String, Object> selectDrugRate(String date);
}
