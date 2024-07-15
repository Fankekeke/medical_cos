package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.HospitalInfo;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.vo.OrderDetailVo;
import cc.mrbird.febs.cos.entity.vo.OrderInfoVo;
import cc.mrbird.febs.cos.service.IHospitalInfoService;
import cc.mrbird.febs.cos.service.IOrderInfoService;
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
@RequestMapping("/cos/order-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderInfoController {

    private final IOrderInfoService orderInfoService;

    private final IHospitalInfoService hospitalInfoService;

    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<OrderInfo> page, OrderInfo orderInfo) {
        return R.ok(orderInfoService.selectOrderPage(page, orderInfo));
    }

    /**
     * 医院销售排行
     *
     * @param year  年份
     * @param month 月份
     * @return 结果
     */
    @GetMapping("/selectSaleRank")
    public R selectSaleRank(@RequestParam("year") String year, @RequestParam("month") String month) {
        return R.ok(orderInfoService.selectSaleRank(year, month));
    }

    /**
     * 用户提交订单
     *
     * @param orderDetailVo 订单信息
     * @return 结果
     */
    @PostMapping("/orderSubmit")
    public R orderSubmit(OrderDetailVo orderDetailVo) throws FebsException {
        return R.ok(orderInfoService.orderSubmit(orderDetailVo));
    }

    /**
     * 平台内添加订单
     *
     * @param orderInfoVo 订单信息
     * @return 结果
     */
    @PostMapping("/platform")
    public R saveOrderByPlatform(OrderInfoVo orderInfoVo) {
        return R.ok(orderInfoService.orderAdd(orderInfoVo, true));
    }

    /**
     * 平台内添加订单
     *
     * @param orderInfoVo 订单信息
     * @return 结果
     */
    @PostMapping("/platform/user")
    public R saveOrderByPlatformHospital(OrderInfoVo orderInfoVo) {
        // 获取医院信息
        HospitalInfo hospitalInfo = hospitalInfoService.getOne(Wrappers.<HospitalInfo>lambdaQuery().eq(HospitalInfo::getUserId, orderInfoVo.getPharmacyId()));
        if (hospitalInfo != null) {
            orderInfoVo.setPharmacyId(hospitalInfo.getId());
        }
        return R.ok(orderInfoService.orderAdd(orderInfoVo, true));
    }

    /**
     * 根据用户月份获取绩效
     *
     * @param staffCode 员工编号
     * @param year      年份
     * @param month     月份
     * @return 结果
     */
    @GetMapping("/performance")
    public R selectPerformanceByStaffCodeMonth(@RequestParam("staffCode") String staffCode, @RequestParam(value = "year", required = false) String year, @RequestParam(value = "month", required = false) String month) {
        return R.ok(orderInfoService.selectPerformanceByStaffCodeMonth(staffCode, year, month));
    }

    /**
     * 统计数据
     *
     * @return 结果
     */
    @GetMapping("/home/data")
    public R selectHomeData() {
        return R.ok(orderInfoService.homeData());
    }

    /**
     * 统计数据
     *
     * @return 结果
     */
    @GetMapping("/home/data/hospital/{hospitalId}")
    public R selectHomeDataByHospital(@PathVariable("hospitalId") Integer hospitalId) {
        return R.ok(orderInfoService.homeDataHospital(hospitalId));
    }

    /**
     * 医生数据统计
     *
     * @param doctorId 医生ID
     * @return 结果
     */
    @GetMapping("/home/data/doctor/{doctorId}")
    public R selectHomeDataByDoctor(@PathVariable("doctorId") Integer doctorId) {
        return R.ok(orderInfoService.selectHomeDataByDoctor(doctorId));
    }

    /**
     * 添加订单
     *
     * @param orderInfoVo 订单信息
     * @return 结果
     */
    @PostMapping("/order")
    public R saveOrder(OrderInfoVo orderInfoVo) {
        return R.ok(orderInfoService.orderAdd(orderInfoVo, false));
    }

    /**
     * 订单付款
     *
     * @param orderCode 订单编号
     * @param staffCode 员工编号
     * @return 结果
     */
    @GetMapping("/payment")
    public R orderPaymentPlatform(@RequestParam("orderCode") String orderCode, @RequestParam(value = "staffCode", required = false) String staffCode) {
        orderInfoService.orderPaymentPlatform(orderCode, staffCode);
        return R.ok(true);
    }

    /**
     * 订单打印小票
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @GetMapping("/receipt/export/{orderId}")
    public R receipt(@PathVariable("orderId") Integer orderId) {
        return R.ok(orderInfoService.receipt(orderId));
    }

    /**
     * 更新订单状态
     *
     * @param orderId 订单ID
     * @param status  状态
     * @return 结果
     */
    @GetMapping("/edit/status")
    public R setOrderStatus(@RequestParam("orderId") Integer orderId, @RequestParam("status") Integer status) {
        return R.ok(orderInfoService.update(Wrappers.<OrderInfo>lambdaUpdate().set(OrderInfo::getOrderStatus, status).eq(OrderInfo::getId, orderId)));
    }

    /**
     * 订单发货
     *
     * @param orderId 订单ID
     * @param remark  物流信息
     * @return 结果
     */
    @GetMapping("/ship")
    public R orderShip(@RequestParam("orderId") Integer orderId, @RequestParam("remark") String remark) {
        return R.ok(orderInfoService.orderShip(orderId, remark));
    }

    /**
     * 获取详情信息
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(orderInfoService.getById(id));
    }

    /**
     * 获取详情信息
     *
     * @param code code
     * @return 结果
     */
    @GetMapping("/detail/{code}")
    public R detail(@PathVariable("code") String code) {
        return R.ok(orderInfoService.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, code)));
    }

    /**
     * 获取信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(orderInfoService.list());
    }

    /**
     * 新增订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PostMapping
    public R save(OrderInfo orderInfo) {
        return R.ok(orderInfoService.save(orderInfo));
    }

    /**
     * 修改订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PutMapping
    public R edit(OrderInfo orderInfo) {
        return R.ok(orderInfoService.updateById(orderInfo));
    }

    /**
     * 删除订单信息
     *
     * @param ids ids
     * @return 订单信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(orderInfoService.removeByIds(ids));
    }

    /**
     * 根据医院获取销售统计
     *
     * @param hospitalId 医院ID
     * @return 结果
     */
    @GetMapping("/selectOrderRateByHospital/{hospitalId}")
    public R selectOrderRateByHospital(@PathVariable("hospitalId") Integer hospitalId) {
        return R.ok(orderInfoService.selectOrderRateByHospital(hospitalId));
    }

    /**
     * 获取药品销售统计
     *
     * @param date 统计日期
     * @return 结果
     */
    @GetMapping("/selectDrugRate")
    public R selectDrugRate(String date) {
        return R.ok(orderInfoService.selectDrugRate(date));
    }
}
