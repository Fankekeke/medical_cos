package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.dao.DoctorInfoMapper;
import cc.mrbird.febs.cos.entity.DoctorInfo;
import cc.mrbird.febs.cos.entity.OrderDetail;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.RegisterInfo;
import cc.mrbird.febs.cos.service.IOrderDetailService;
import cc.mrbird.febs.cos.service.IOrderInfoService;
import cc.mrbird.febs.cos.service.IRegisterInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/register-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegisterInfoController {

    private final IRegisterInfoService registerInfoService;

    private final IOrderInfoService orderInfoService;

    private final IOrderDetailService orderDetailService;

    private final DoctorInfoMapper doctorInfoMapper;

    /**
     * 分页获取挂号记录信息
     *
     * @param page         分页对象
     * @param registerInfo 挂号记录信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<RegisterInfo> page, RegisterInfo registerInfo) {
        return R.ok(registerInfoService.selectRegisterPage(page, registerInfo));
    }

    /**
     * 按月统计医生挂号记录
     *
     * @param year  年份
     * @param month 月份
     * @return 结果
     */
    @GetMapping("/selectRegisterByDoctorRank")
    public R selectRegisterByDoctorRank(@RequestParam("year") String year, @RequestParam("month") String month) {
        return R.ok(registerInfoService.selectRegisterByDoctorRank(year, month));
    }

    /**
     * 挂号添加订单
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PostMapping("/register/order")
    @Transactional(rollbackFor = Exception.class)
    public R registerOrderAdd(OrderInfo orderInfo) {
        // 获取挂号信息
        RegisterInfo registerInfo = registerInfoService.getById(orderInfo.getRegisterId());
        // 订单信息
        orderInfo.setCode("OR-" + System.currentTimeMillis());
        // 所属用户
        orderInfo.setUserId(registerInfo.getUserId());
        orderInfo.setStaffId(registerInfo.getStaffId());
        // 创建时间
        orderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        // 订单状态
        orderInfo.setOrderStatus(0);
        // 医生信息
//        DoctorInfo doctorInfo = doctorInfoMapper.selectOne(Wrappers.<DoctorInfo>lambdaQuery().eq(DoctorInfo::getUserId, orderInfo.getStaffId()));
        DoctorInfo doctorInfo = doctorInfoMapper.selectById(registerInfo.getStaffId());
        if (doctorInfo != null) {
            // 所属医院
            orderInfo.setPharmacyId(doctorInfo.getHospitalId());
        }
        // 添加订单详情
        List<OrderDetail> orderDetailList = JSONUtil.toList(orderInfo.getOrderItem(), OrderDetail.class);
        // 计算药品总价格
        BigDecimal totalCost = BigDecimal.ZERO;
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetail.setAllPrice(NumberUtil.mul(orderDetail.getUnitPrice(), orderDetail.getQuantity()));
            totalCost = NumberUtil.add(totalCost, orderDetail.getAllPrice());
        }

        // 所属挂号
        orderInfo.setTotalCost(totalCost);
        orderInfoService.save(orderInfo);
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetail.setOrderId(orderInfo.getId());
        }
        orderDetailService.saveBatch(orderDetailList);

        // 更新挂号信息
        registerInfo.setStatus("3");
        registerInfo.setDrugPrice(totalCost);
        return R.ok(registerInfoService.updateById(registerInfo));
    }

    /**
     * 查询挂号记录信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(registerInfoService.getById(id));
    }

    /**
     * 查询挂号记录信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(registerInfoService.list());
    }

    /**
     * 添加挂号申请
     *
     * @param registerInfo 挂号信息
     * @return 结果
     */
    @PostMapping("/registerOrderAdd")
    public R registerOrderAdd(RegisterInfo registerInfo) {
        return R.ok(registerInfoService.registerOrderAdd(registerInfo));
    }

    /**
     * `
     * 新增挂号记录信息
     *
     * @param registerInfo 挂号记录信息
     * @return 结果
     */
    @PostMapping
    public R save(RegisterInfo registerInfo) {
        registerInfo.setCode("REG-" + System.currentTimeMillis());
        return R.ok(registerInfoService.save(registerInfo));
    }

    /**
     * 根据编号更新状态
     *
     * @param registerInfo 根据编号更新状态
     * @return 结果
     */
    @PutMapping("/updateRegisterByCode")
    public R updateRegisterByCode(RegisterInfo registerInfo) {
        return R.ok(registerInfoService.update(Wrappers.<RegisterInfo>lambdaUpdate().set(RegisterInfo::getStatus, registerInfo.getStatus()).eq(RegisterInfo::getCode, registerInfo.getCode())));
    }

    /**
     * 订单支付回调
     *
     * @param code 订单编号
     * @return 结果
     */
    @GetMapping("/payRollBack")
    public R payRollBack(@RequestParam("code") String code) {
        // 判断编号未挂号还是订单
        if (code.indexOf("REG") != -1) {
            return R.ok(registerInfoService.update(Wrappers.<RegisterInfo>lambdaUpdate().set(RegisterInfo::getStatus, "1").set(RegisterInfo::getPayDate, DateUtil.formatDateTime(new Date())).eq(RegisterInfo::getCode, code)));
        } else {
            OrderInfo orderInfo = orderInfoService.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, code));
            if (orderInfo.getRegisterId() != null) {
                registerInfoService.update(Wrappers.<RegisterInfo>lambdaUpdate().set(RegisterInfo::getStatus, "4").set(RegisterInfo::getPayDate, DateUtil.formatDateTime(new Date())).eq(RegisterInfo::getId, orderInfo.getRegisterId()));
            }
            DoctorInfo doctorInfo = doctorInfoMapper.selectById(orderInfo.getStaffId());
            orderInfoService.orderPaymentOnline(code, doctorInfo != null ? doctorInfo.getCode() : null);
            return R.ok(orderInfoService.update(Wrappers.<OrderInfo>lambdaUpdate().set(OrderInfo::getOrderStatus, "1").eq(OrderInfo::getCode, code)));
        }
    }

    /**
     * 修改挂号记录信息
     *
     * @param registerInfo 挂号记录信息
     * @return 结果
     */
    @PutMapping
    public R edit(RegisterInfo registerInfo) {
        return R.ok(registerInfoService.updateById(registerInfo));
    }

    /**
     * 删除挂号记录信息
     *
     * @param ids ids
     * @return 挂号记录信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(registerInfoService.removeByIds(ids));
    }

}
