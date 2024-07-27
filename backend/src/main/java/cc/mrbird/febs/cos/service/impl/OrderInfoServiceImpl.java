package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.dao.RegisterInfoMapper;
import cc.mrbird.febs.cos.dao.ScheduleInfoMapper;
import cc.mrbird.febs.cos.dao.UserInfoMapper;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.OrderInfoMapper;
import cc.mrbird.febs.cos.entity.vo.OrderDetailVo;
import cc.mrbird.febs.cos.entity.vo.OrderInfoVo;
import cc.mrbird.febs.cos.entity.vo.OrderSubVo;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {
    private final UserInfoMapper userInfoMapper;

    private final IOrderDetailService orderDetailService;

    private final IPharmacyInventoryService pharmacyInventoryService;

    private final IInventoryStatisticsService inventoryStatisticsService;

    private final ILogisticsInfoService logisticsInfoService;

    private final IDoctorInfoService doctorInfoService;

    private final IHospitalInfoService hospitalInfoService;

    private final IOfficeInfoService officeInfoService;

    private final IRegisterInfoService registerInfoService;

    private final IBulletinInfoService bulletinInfoService;

    private final RegisterInfoMapper registerInfoMapper;

    private final ScheduleInfoMapper scheduleMapper;

    private final IDrugInfoService drugInfoService;


    /**
     * 分页获取订单信息
     *
     * @param page     分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<OrderInfo> page, OrderInfo orderInfo) {
        return baseMapper.selectOrderPage(page, orderInfo);
    }

    /**
     * 获取医院销售排名
     *
     * @param year  年份
     * @param month 月份
     * @return 结果
     */
    @Override
    public List<HospitalInfo> selectSaleRank(String year, String month) {
        // 获取所有医院信息
        List<HospitalInfo> hospitalInfoList = hospitalInfoService.list();
        if (CollectionUtil.isEmpty(hospitalInfoList)) {
            return hospitalInfoList;
        }

        // 获取本月已支付订单
        List<OrderInfo> orderInfoList = baseMapper.selectOrderHospitalByDate(year, month);
        Map<Integer, List<OrderInfo>> orderInfoMap = orderInfoList.stream().collect(Collectors.groupingBy(OrderInfo::getPharmacyId));
        for (HospitalInfo hospitalInfo : hospitalInfoList) {

            if (CollectionUtil.isEmpty(orderInfoMap) || CollectionUtil.isEmpty(orderInfoMap.get(hospitalInfo.getId()))) {
                hospitalInfo.setSalesNum(0);
                hospitalInfo.setSalesTotal(BigDecimal.ZERO);
                continue;
            }

            List<OrderInfo> thisOrderInfos = orderInfoMap.get(hospitalInfo.getId());
            BigDecimal salesTotal = thisOrderInfos.stream().map(OrderInfo::getTotalCost).reduce(BigDecimal.ZERO, BigDecimal::add);
            hospitalInfo.setSalesNum(thisOrderInfos.size());
            hospitalInfo.setSalesTotal(salesTotal);
        }

        return hospitalInfoList.stream().sorted(Comparator.comparing(HospitalInfo::getSalesTotal).reversed()).collect(Collectors.toList());
    }

    /**
     * 添加订单信息
     *
     * @param orderInfoVo 订单信息
     * @param flag 是否付款完成（0.否 1.是）
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean orderAdd(OrderInfoVo orderInfoVo, boolean flag) {
        // 添加订单信息
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderStatus(flag ? 3 : 0);
        orderInfo.setCode("OR-" + System.currentTimeMillis());
        orderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        orderInfo.setStaffId(orderInfoVo.getStaffId());
        orderInfo.setPharmacyId(orderInfoVo.getPharmacyId());
        // 所属用户
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderInfoVo.getUserId()));
        if (userInfo != null) {
            orderInfo.setUserId(userInfo.getId());
        }
        this.save(orderInfo);
        // 添加订单详情信息
        List<OrderDetail> detailList = JSONUtil.toList(orderInfoVo.getOrderDetailList(), OrderDetail.class);
        if (CollectionUtil.isNotEmpty(detailList)) {
            BigDecimal totalCost = BigDecimal.ONE;
            // 计算药品总价，绑定订单
            for (OrderDetail e : detailList) {
                e.setOrderId(orderInfo.getId());
                e.setAllPrice(e.getUnitPrice().multiply(BigDecimal.valueOf(e.getQuantity())));
                totalCost = totalCost.add(e.getAllPrice());
            }
            orderInfo.setTotalCost(totalCost);
            orderDetailService.saveBatch(detailList);
        }
        // 重新更新订单信息
        boolean result = this.updateById(orderInfo);
        if (flag) {
            this.orderPaymentPlatform(orderInfo.getCode(), null);
        }

        return result;
    }

    /**
     * 设置数据
     *
     * @param type 类型
     */
    @Override
    public void setData(String type) {
        List<DoctorInfo> doctorList = doctorInfoService.list();
        List<HospitalInfo> hospitalInfoList = hospitalInfoService.list();
        List<OfficeInfo> officeList = officeInfoService.list();

        Map<String, HospitalInfo> hospitalInfoMap = hospitalInfoList.stream().collect(Collectors.toMap(HospitalInfo::getHospitalName, e -> e, (k1, k2) -> k2));
        Map<String, OfficeInfo> officeInfoMap = officeList.stream().collect(Collectors.toMap(OfficeInfo::getOfficesName, e -> e, (k1, k2) -> k2));

        // 更新医生信息
        for (DoctorInfo doctorInfo : doctorList) {
            if (StrUtil.isNotEmpty(doctorInfo.getHospitalName())) {
                HospitalInfo hospitalInfo = hospitalInfoMap.get(doctorInfo.getHospitalName());
                if (hospitalInfo != null) {
                    doctorInfo.setHospitalId(hospitalInfo.getId());
                }

            }
            if (StrUtil.isNotEmpty(doctorInfo.getOfficesName())) {
                OfficeInfo officeInfo = officeInfoMap.get(doctorInfo.getOfficesName());
                if (officeInfo != null) {
                    doctorInfo.setOfficesId(officeInfo.getId());
                }
            }
        }
        doctorInfoService.updateBatchById(doctorList);

        // 更新科室信息
        for (OfficeInfo officeInfo : officeList) {
            if (StrUtil.isNotEmpty(officeInfo.getHospitalName())) {
                HospitalInfo hospitalInfo = hospitalInfoMap.get(officeInfo.getHospitalName());
                if (hospitalInfo != null) {
                    officeInfo.setHospitalId(hospitalInfo.getId());
                }
            }
        }
        officeInfoService.updateBatchById(officeList);

    }

    /**
     * 根据医院获取销售统计
     *
     * @param hospitalId 医院ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectOrderRateByHospital(Integer hospitalId) {
        // 根据医院获取订单
        List<OrderInfo> orderInfoList = this.list(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getPharmacyId, hospitalId));

        List<DrugInfo> drugList = drugInfoService.list();

        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("price", Collections.emptyList());
                put("num", Collections.emptyList());
            }
        };
        if (CollectionUtil.isEmpty(orderInfoList) || CollectionUtil.isEmpty(drugList)) {
            return result;
        }

        // 根据订单获取订单详情
        List<Integer> orderIds = orderInfoList.stream().map(OrderInfo::getId).collect(Collectors.toList());
        List<OrderDetail> orderDetailList = orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery().in(OrderDetail::getOrderId, orderIds));
        Map<Integer, List<OrderDetail>> orderDetailMap = orderDetailList.stream().collect(Collectors.groupingBy(OrderDetail::getDrugId));
        // 药物销量统计
        List<LinkedHashMap<String, Object>> numList = new ArrayList<>();
        List<LinkedHashMap<String, Object>> priceList = new ArrayList<>();

        for (DrugInfo drugInfo : drugList) {
            // 根据药品获取记录
            List<OrderDetail> detailList = orderDetailMap.get(drugInfo.getId());
            LinkedHashMap<String, Object> numItem = new LinkedHashMap<String, Object>() {
                {
                    put("name", drugInfo.getName());
                    put("value", 0);
                }
            };

            LinkedHashMap<String, Object> priceItem = new LinkedHashMap<String, Object>() {
                {
                    put("name", drugInfo.getName());
                    put("value", BigDecimal.ZERO);
                }
            };

            if (CollectionUtil.isEmpty(detailList)) {
                numList.add(numItem);
                priceList.add(priceItem);
                continue;
            }
            numItem.put("value", detailList.stream().mapToInt(OrderDetail::getQuantity).sum());
            priceItem.put("value", detailList.stream().map(OrderDetail::getAllPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
            numList.add(numItem);
            priceList.add(priceItem);
        }
        result.put("price", priceList);
        result.put("num", numList);
        return result;
    }

    /**
     * 获取药品销售统计
     *
     * @param date 统计日期
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectDrugRate(String date) {
        date = date + "-01";
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("pieTypeNumRate", Collections.emptyList());
                put("pieDrugNumRate", Collections.emptyList());
                put("pieTypePriceRate", Collections.emptyList());
                put("pieDrugPriceRate", Collections.emptyList());
                put("saleNum", Collections.emptyList());
                put("salePrice", Collections.emptyList());
            }
        };
        // 获取统计年份与月份
        int year = DateUtil.thisYear();
        int month = DateUtil.thisMonth();

        if (StrUtil.isNotEmpty(date)) {
            year = DateUtil.year(DateUtil.parseDate(date));
            month = DateUtil.month(DateUtil.parseDate(date)) + 1;
        }

        // 药品销售量统计
        List<LinkedHashMap<String, Object>> saleNum = new ArrayList<>();
        // 药品销售价格统计
        List<LinkedHashMap<String, Object>> salePrice = new ArrayList<>();

        // 药品信息
        List<DrugInfo> drugList = drugInfoService.list();
        Map<Integer, DrugInfo> drugMap = drugList.stream().collect(Collectors.toMap(DrugInfo::getId, e -> e));

        int finalYear = year;
        int finalMonth = month;
        drugMap.forEach((key, value) -> {
            saleNum.add(new LinkedHashMap<String, Object>() {
                {
                    put("name", value.getName());
                    put("value", baseMapper.selectDrugNumSaleByMonth(finalYear, finalMonth, key));
                }
            });
            salePrice.add(new LinkedHashMap<String, Object>() {
                {
                    put("name", value.getName());
                    put("value", baseMapper.selectDrugPriceSaleByMonth(finalYear, finalMonth, key));
                }
            });
        });
        result.put("saleNum", saleNum);
        result.put("salePrice", salePrice);

        // 药品按类型分类
        // Map<Integer, List<DrugInfo>> drugTypeMap = drugList.stream().collect(Collectors.groupingBy(DrugInfo::getClassification));
        LinkedHashMap<Integer, String> drugTypeMap = new LinkedHashMap<Integer, String>() {
            {
                put(1, "中药材");
                put(2, "中药饮片");
                put(3, "中西成药");
                put(4, "化学原料药");
                put(5, "抗生素");
                put(6, "生化药品");
                put(7, "放射性药品");
                put(8, "血清");
                put(9, "诊断药品");
            }
        };

        // 本月订单
        List<OrderInfo> orderInfoList = baseMapper.selectOrderByYearMonth(year, month);
        if (CollectionUtil.isEmpty(orderInfoList)) {
            return result;
        }
        List<Integer> orderIds = orderInfoList.stream().map(OrderInfo::getId).collect(Collectors.toList());
        List<OrderDetail> detailList = orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery().in(OrderDetail::getOrderId, orderIds));
        // 设置药品类型
        for (OrderDetail orderDetail : detailList) {
            if (orderDetail.getDrugId() == null || drugMap.get(orderDetail.getDrugId()) == null) {
                continue;
            }
            orderDetail.setClassification(drugMap.get(orderDetail.getDrugId()).getClassification());
        }
        // 按药品ID分类
        Map<Integer, List<OrderDetail>> detailMap = detailList.stream().collect(Collectors.groupingBy(OrderDetail::getDrugId));
        // 按药品类型分类
        Map<Integer, List<OrderDetail>> detailTypeMap = detailList.stream().collect(Collectors.groupingBy(OrderDetail::getClassification));

        // 药品数量统计
        List<LinkedHashMap<String, Object>> pieDrugNumRate = new ArrayList<>();
        // 药品类型数量统计
        List<LinkedHashMap<String, Object>> pieTypeNumRate = new ArrayList<>();
        // 药品价格统计
        List<LinkedHashMap<String, Object>> pieDrugPriceRate = new ArrayList<>();
        // 药品类型价格统计
        List<LinkedHashMap<String, Object>> pieTypePriceRate = new ArrayList<>();

        detailMap.forEach((key, value) -> {
            // 药品数量统计
            pieDrugNumRate.add(new LinkedHashMap<String, Object>() {
                {
                    put("name", drugMap.get(key).getName());
                    put("value", value.stream().mapToInt(OrderDetail::getQuantity).sum());
                }
            });

            // 药品价格统计
            pieDrugPriceRate.add(new LinkedHashMap<String, Object>() {
                {
                    put("name", drugMap.get(key).getName());
                    put("value", value.stream().map(OrderDetail::getAllPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                }
            });
        });

        drugTypeMap.forEach((key, value) -> {
            List<OrderDetail> orderDetailList = detailTypeMap.get(key);
            // 药品类型数量统计
            pieTypeNumRate.add(new LinkedHashMap<String, Object>() {
                {
                    put("name", value);
                    put("value", CollectionUtil.isEmpty(orderDetailList) ? 0 : orderDetailList.stream().mapToInt(OrderDetail::getQuantity).sum());
                }
            });
            // 药品类型价格统计
            pieTypePriceRate.add(new LinkedHashMap<String, Object>() {
                {
                    put("name", value);
                    put("value", CollectionUtil.isEmpty(orderDetailList) ? BigDecimal.ZERO : orderDetailList.stream().map(OrderDetail::getAllPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                }
            });
        });
        detailTypeMap.forEach((key, value) -> {

        });
        result.put("pieTypeNumRate", pieTypeNumRate);
        result.put("pieDrugNumRate", pieDrugNumRate);
        result.put("pieTypePriceRate", pieTypePriceRate);
        result.put("pieDrugPriceRate", pieDrugPriceRate);

        return result;
    }

    /**
     * 根据用户月份获取绩效
     *
     * @param staffCode 员工编号
     * @param year      年份
     * @param month     月份
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, BigDecimal> selectPerformanceByStaffCodeMonth(String staffCode, String year, String month) {
        // 返回数据
        LinkedHashMap<String, BigDecimal> result = new LinkedHashMap<String, BigDecimal>() {
            {
                put("before", BigDecimal.ZERO);
                put("after", BigDecimal.ZERO);
            }
        };
        if (StrUtil.isEmpty(year)) {
            year = StrUtil.toString(DateUtil.year(new Date()));
        }
        if (StrUtil.isEmpty(month)) {
            month = StrUtil.toString(DateUtil.month(new Date()) + 1);
        }
        if (StrUtil.isEmpty(staffCode)) {
            return result;
        }

//        List<InventoryStatistics> inventoryList = baseMapper.selectPerformanceByStaffCodeMonth(staffCode, year, month);
//        if (CollectionUtil.isEmpty(inventoryList)) {
//            return result;
//        }
//        BigDecimal totalPrice = inventoryList.stream().map(e -> NumberUtil.mul(e.getUnitPrice(), e.getQuantity())).reduce(BigDecimal.ZERO, BigDecimal::add);
//        result.put("before", NumberUtil.round(totalPrice, 2));
//        result.put("after", NumberUtil.mul(totalPrice, new BigDecimal("0.2")));
        return result;
    }

    /**
     * 主页数据
     *
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> homeData() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        // 总挂号数量
        List<String> status = new ArrayList<>();
        status.add("1");
        status.add("2");
        status.add("3");
        status.add("4");
        result.put("registerNum", registerInfoService.count(Wrappers.<RegisterInfo>lambdaQuery().in(RegisterInfo::getStatus, status)));
        // 总收益
        result.put("orderPrice", baseMapper.selectOrderPrice(null));
        // 医生数量
        result.put("doctorNum", doctorInfoService.count());
        // 医院数量
        result.put("hospitalNum", hospitalInfoService.count(Wrappers.<HospitalInfo>lambdaQuery()));

        // 本月挂号数量
        List<RegisterInfo> registerInfoList = registerInfoMapper.selectRegisterByMonth(null);
        List<OrderInfo> orderList = baseMapper.selectOrderByMonth(null);
        result.put("monthOrderNum", CollectionUtil.isEmpty(registerInfoList) ? 0 : registerInfoList.size());
        BigDecimal orderPrice = orderList.stream().map(OrderInfo::getTotalCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal registerPrice = registerInfoList.stream().map(RegisterInfo::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 获取本月收益
        result.put("monthOrderPrice", orderPrice.add(registerPrice));

        // 本年挂号数量
        List<RegisterInfo> registerYearList = registerInfoMapper.selectRegisterByYear(null);
        List<OrderInfo> orderYearList = baseMapper.selectOrderByYear(null);
        result.put("yearOrderNum", CollectionUtil.isEmpty(registerYearList) ? 0 : registerYearList.size());
        // 本年总收益
        BigDecimal orderYearPrice = orderYearList.stream().map(OrderInfo::getTotalCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal registerYearPrice = registerYearList.stream().map(RegisterInfo::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("yearOrderPrice", orderYearPrice.add(registerYearPrice));

        // 公告信息
        result.put("bulletin", bulletinInfoService.list(Wrappers.<BulletinInfo>lambdaQuery().eq(BulletinInfo::getRackUp, 1)));

        // 近十天内订单统计
        result.put("orderNumWithinDays", baseMapper.selectOrderNumWithinDays(null));
        // 近十天内收益统计
        result.put("orderPriceWithinDays", baseMapper.selectOrderPriceWithinDays(null));
        return result;
    }

    /**
     * 主页数据
     *
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> homeDataHospital(Integer hospitalId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        // 医院信息
        HospitalInfo hospitalInfo = hospitalInfoService.getOne(Wrappers.<HospitalInfo>lambdaQuery().eq(HospitalInfo::getUserId, hospitalId));

        // 总挂号数量
        List<String> status = new ArrayList<>();
        status.add("1");
        status.add("2");
        status.add("3");
        status.add("4");
        result.put("registerNum", registerInfoService.count(Wrappers.<RegisterInfo>lambdaQuery().in(RegisterInfo::getStatus, status).eq(RegisterInfo::getDeptId, hospitalInfo.getId())));
        // 总收益
        result.put("orderPrice", baseMapper.selectOrderPrice(hospitalInfo.getId()));
        // 医生数量
        result.put("doctorNum", doctorInfoService.count(Wrappers.<DoctorInfo>lambdaQuery().eq(DoctorInfo::getHospitalId, hospitalInfo.getId())));

        // 本月挂号数量
        List<RegisterInfo> registerInfoList = registerInfoMapper.selectRegisterByMonth(hospitalInfo.getId());
        List<OrderInfo> orderList = baseMapper.selectOrderByMonth(hospitalInfo.getId());
        result.put("monthOrderNum", CollectionUtil.isEmpty(registerInfoList) ? 0 : registerInfoList.size());
        BigDecimal orderPrice = orderList.stream().map(OrderInfo::getTotalCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal registerPrice = registerInfoList.stream().map(RegisterInfo::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 获取本月收益
        result.put("monthOrderPrice", orderPrice.add(registerPrice));

        // 本年挂号数量
        List<RegisterInfo> registerYearList = registerInfoMapper.selectRegisterByYear(hospitalInfo.getId());
        List<OrderInfo> orderYearList = baseMapper.selectOrderByYear(hospitalInfo.getId());
        result.put("yearOrderNum", CollectionUtil.isEmpty(registerYearList) ? 0 : registerYearList.size());
        // 本年总收益
        BigDecimal orderYearPrice = orderYearList.stream().map(OrderInfo::getTotalCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal registerYearPrice = registerYearList.stream().map(RegisterInfo::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("yearOrderPrice", orderYearPrice.add(registerYearPrice));

        // 公告信息
        result.put("bulletin", bulletinInfoService.list(Wrappers.<BulletinInfo>lambdaQuery().eq(BulletinInfo::getRackUp, 1)));

        // 近十天内订单统计
        result.put("orderNumWithinDays", baseMapper.selectOrderNumWithinDays(hospitalInfo.getId()));
        // 近十天内收益统计
        result.put("orderPriceWithinDays", baseMapper.selectOrderPriceWithinDays(hospitalInfo.getId()));
        return result;
    }

    /**
     * 医生数据统计
     *
     * @param doctorId 医生ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectHomeDataByDoctor(Integer doctorId) {
        // 医生信息
        DoctorInfo doctorInfo = doctorInfoService.getOne(Wrappers.<DoctorInfo>lambdaQuery().eq(DoctorInfo::getUserId, doctorId));

        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("doctorInfo", doctorInfo);

        // 所属医院
        result.put("hospitalName", doctorInfo.getHospitalName());
        // 所属科室
        result.put("officesName", doctorInfo.getOfficesName());

        // 排班数量
        List<ScheduleInfo> scheduleList = scheduleMapper.selectList(Wrappers.<ScheduleInfo>lambdaQuery().eq(ScheduleInfo::getStaffIds, doctorInfo.getId()).eq(ScheduleInfo::getStatus, 0));
        Integer scheduleNum = scheduleList.size();
        result.put("scheduleNum", scheduleNum);
        // 挂号数量
        List<Integer> scheduleIds = scheduleList.stream().map(ScheduleInfo::getId).collect(Collectors.toList());
        // 挂号信息
        List<RegisterInfo> registerList = new ArrayList<>();
        if (CollectionUtil.isEmpty(scheduleIds)) {
            result.put("registerNum", 0);
        } else {
            List<String> status = Arrays.asList("1", "2", "3", "4");
            registerList = registerInfoService.list(Wrappers.<RegisterInfo>lambdaQuery().in(RegisterInfo::getScheduleId, scheduleIds).in(RegisterInfo::getStatus, status));
            Integer registerNum = registerList.size();
            result.put("registerNum", registerNum);
        }

        List<RegisterInfo> toMonthList = registerList.stream().filter(e -> DateUtil.parseDate(e.getRegisterDate()).isAfter(new Date())).collect(Collectors.toList());
        // 本月处方金额
        BigDecimal totalCost = registerList.stream().map(RegisterInfo::getDrugPrice).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("totalCost", totalCost);
        // 本月挂号金额
        BigDecimal totalRegisterCost = registerList.stream().map(RegisterInfo::getPrice).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("totalRegisterCost", totalRegisterCost);
        // 我的排班
        result.put("scheduleList", scheduleList);
        // 我的挂号【未过期】
        List<Integer> registerIds = toMonthList.stream().map(RegisterInfo::getId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(registerIds)) {
            result.put("registerList", registerInfoMapper.selectRegisterList(registerIds));
        } else {
            result.put("registerList", Collections.emptyList());
        }
        return result;
    }

    /**
     * 平台内订单付款
     *
     * @param orderCode 订单编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPaymentPlatform(String orderCode, String staffCode) {
        if (StrUtil.isEmpty(orderCode)) {
            return;
        }
        // 获取订单信息
        OrderInfo orderInfo = this.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, orderCode));
//        if (StrUtil.isEmpty(staffCode)) {
//            List<StaffInfo> staffInfoList = staffInfoMapper.selectList(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getPharmacyId, orderInfo.getPharmacyId()));
//            staffCode = staffInfoList.get(0).getCode();
//        }
        // 订单详情
        List<OrderDetail> detailList = orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, orderInfo.getId()));
        Map<Integer, Integer> detailMap = detailList.stream().collect(Collectors.toMap(OrderDetail::getDrugId, OrderDetail::getQuantity));
        // 根据药品ID获取库存信息
        List<PharmacyInventory> inventoryList = pharmacyInventoryService.list(Wrappers.<PharmacyInventory>lambdaQuery().in(PharmacyInventory::getDrugId, detailMap.keySet()).eq(PharmacyInventory::getPharmacyId, orderInfo.getPharmacyId()));
        List<InventoryStatistics> statisticsList = new ArrayList<>();

        String finalStaffCode = staffCode;
        inventoryList.forEach(e -> {
            InventoryStatistics inventoryStatistics = new InventoryStatistics();
            inventoryStatistics.setDrugId(e.getDrugId());
            inventoryStatistics.setPharmacyId(e.getPharmacyId());
            inventoryStatistics.setQuantity(detailMap.get(e.getDrugId()));
            inventoryStatistics.setStorageType(1);
            inventoryStatistics.setCustodian(finalStaffCode);
            inventoryStatistics.setCreateDate(DateUtil.formatDateTime(new Date()));
            statisticsList.add(inventoryStatistics);
            e.setReserve(e.getReserve() - detailMap.get(e.getDrugId()));
        });
        // 修改库存信息
        pharmacyInventoryService.updateBatchById(inventoryList);
        // 添加库房统计
        inventoryStatisticsService.saveBatch(statisticsList);
        orderInfo.setOrderStatus(3);
        this.updateById(orderInfo);
    }

    /**
     * 线上订单付款
     *
     * @param orderCode 订单编号
     * @param staffCode 员工编号
     */
    @Override
    public void orderPaymentOnline(String orderCode, String staffCode) {
        if (StrUtil.isEmpty(orderCode)) {
            return;
        }
        // 获取订单信息
        OrderInfo orderInfo = this.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, orderCode));
        // 订单详情
        List<OrderDetail> detailList = orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, orderInfo.getId()));
        Map<Integer, Integer> detailMap = detailList.stream().collect(Collectors.toMap(OrderDetail::getDrugId, OrderDetail::getQuantity));
        // 根据药品ID获取库存信息
        List<PharmacyInventory> inventoryList = pharmacyInventoryService.list(Wrappers.<PharmacyInventory>lambdaQuery().in(PharmacyInventory::getDrugId, detailMap.keySet()).eq(PharmacyInventory::getPharmacyId, orderInfo.getPharmacyId()));
        List<InventoryStatistics> statisticsList = new ArrayList<>();

        String finalStaffCode = staffCode;
        inventoryList.forEach(e -> {
            InventoryStatistics inventoryStatistics = new InventoryStatistics();
            inventoryStatistics.setDrugId(e.getDrugId());
            inventoryStatistics.setPharmacyId(e.getPharmacyId());
            inventoryStatistics.setQuantity(detailMap.get(e.getDrugId()));
            inventoryStatistics.setStorageType(1);
            inventoryStatistics.setCustodian(finalStaffCode);
            inventoryStatistics.setCreateDate(DateUtil.formatDateTime(new Date()));
            statisticsList.add(inventoryStatistics);
            e.setReserve(e.getReserve() - detailMap.get(e.getDrugId()));
        });
        // 修改库存信息
        pharmacyInventoryService.updateBatchById(inventoryList);
        // 添加库房统计
        inventoryStatisticsService.saveBatch(statisticsList);
    }

    /**
     * 获取用户订单统计
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectOrderRateByUser(Integer userId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        if (userId == null) {
            return result;
        }
        // 获取用户信息
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        if (userInfo == null) {
            return result;
        }
        result.put("user", userInfo);
        // 获取用户订单
        List<OrderInfo> orderList = this.list(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getUserId, userInfo.getUserId()));
        if (CollectionUtil.isEmpty(orderList)) {
            return result;
        }
        LinkedHashMap<String, Object> rate = new LinkedHashMap<>();
        return null;
    }

    /**
     * 用户提交订单
     *
     * @param orderDetailVo 订单信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean orderSubmit(OrderDetailVo orderDetailVo) throws FebsException {
        // 获取用户信息
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderDetailVo.getUserId()));
        if (null == userInfo) {
            throw new FebsException("未获取到用户信息");
        }
        // 获取订单信息
        List<OrderSubVo> orderSubVos = JSONUtil.toList(orderDetailVo.getDrugString(), OrderSubVo.class);
        // 根据医院分组
        Map<Integer, List<OrderSubVo>> orderSubMap = orderSubVos.stream().collect(Collectors.groupingBy(OrderSubVo::getPharmacyId));
        // 设置要添加订单
        List<OrderInfo> orderList = new ArrayList<>();
        // 添加的订单详情
        List<OrderDetail> orderDetailList = new ArrayList<>();

        orderSubMap.forEach((key, value) -> {
            OrderInfo orderItem = new OrderInfo();
            orderItem.setCode(StrUtil.toString(System.currentTimeMillis()) + key);
            orderItem.setPharmacyId(key);
            orderItem.setCreateDate(DateUtil.formatDateTime(new Date()));
            orderItem.setOrderStatus(0);
            orderItem.setUserId(userInfo.getId());
            this.save(orderItem);
            // 总价格
            BigDecimal totalCost = BigDecimal.ZERO;
            for (OrderSubVo orderSubItem: value) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setDrugId(orderSubItem.getDrugId());
                orderDetail.setQuantity(orderSubItem.getTotal().intValue());
                orderDetail.setUnitPrice(orderSubItem.getUnitPrice());
                orderDetail.setOrderId(orderItem.getId());
                orderDetail.setAllPrice(orderDetail.getUnitPrice().multiply(orderSubItem.getTotal()));
                totalCost = totalCost.add(orderDetail.getAllPrice());
                orderDetailList.add(orderDetail);
            }
            orderItem.setTotalCost(totalCost);
            this.updateById(orderItem);
        });
        return orderDetailService.saveBatch(orderDetailList);
    }

    /**
     * 订单付款
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean orderPayment(String orderCode) {
        if (StrUtil.isEmpty(orderCode)) {
            return false;
        }
        // 获取订单信息
        OrderInfo orderInfo = this.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, orderCode));
        // 修改订单状态
        orderInfo.setOrderStatus(1);
        // 订单详情
        List<OrderDetail> detailList = orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, orderInfo.getId()));
        Map<Integer, Integer> detailMap = detailList.stream().collect(Collectors.toMap(OrderDetail::getDrugId, OrderDetail::getQuantity));
        // 根据药品ID获取库存信息
        List<PharmacyInventory> inventoryList = pharmacyInventoryService.list(Wrappers.<PharmacyInventory>lambdaQuery().in(PharmacyInventory::getDrugId, detailMap.keySet()).eq(PharmacyInventory::getPharmacyId, orderInfo.getPharmacyId()));
        List<InventoryStatistics> statisticsList = new ArrayList<>();

        inventoryList.forEach(e -> {
            e.setReserve(e.getReserve() - detailMap.get(e.getDrugId()));
            InventoryStatistics inventoryStatistics = new InventoryStatistics();
            inventoryStatistics.setDrugId(e.getDrugId());
            inventoryStatistics.setPharmacyId(e.getPharmacyId());
            inventoryStatistics.setQuantity(e.getReserve());
            inventoryStatistics.setStorageType(1);
            inventoryStatistics.setCreateDate(DateUtil.formatDateTime(new Date()));
            statisticsList.add(inventoryStatistics);
        });
        // 订单物流信息初始化
        LogisticsInfo logisticsInfo = new LogisticsInfo();
        logisticsInfo.setOrderId(orderInfo.getId());
        logisticsInfo.setRemark("订单正在揽收");
        logisticsInfo.setCurrentLogistics(1);
        logisticsInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        logisticsInfoService.save(logisticsInfo);
        // 修改库存信息
        pharmacyInventoryService.updateBatchById(inventoryList);
        // 添加库房统计
        inventoryStatisticsService.saveBatch(statisticsList);

        return this.updateById(orderInfo);
    }

    /**
     * 订单打印小票
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> receipt(Integer orderId) {
        if (orderId == null) {
            return null;
        }
        // 获取订单信息
        OrderInfo orderInfo = this.getById(orderId);
        // 客户信息
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getId, orderInfo.getUserId()));
        orderInfo.setUserName(userInfo.getName());
        // 订单详情
//        List<LinkedHashMap<String, Object>> detailList = orderDetailService.selectDetailByOrder(orderId);
        // 返回数据
        return new LinkedHashMap<String, Object>() {
            {
                put("order", orderInfo);
//                put("detail", detailList);
            }
        };
    }

    /**
     * 订单发货
     *
     * @param orderId 订单ID
     * @param remark  物流信息
     * @return 结果
     */
    @Override
    public boolean orderShip(Integer orderId, String remark) {
        logisticsInfoService.update(Wrappers.<LogisticsInfo>lambdaUpdate().set(LogisticsInfo::getCurrentLogistics, 0).eq(LogisticsInfo::getOrderId, orderId));
        LogisticsInfo logisticsInfo = new LogisticsInfo();
        logisticsInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        logisticsInfo.setOrderId(orderId);
        logisticsInfo.setRemark(remark);
        logisticsInfo.setCurrentLogistics(1);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(orderId);
        orderInfo.setOrderStatus(2);
        this.updateById(orderInfo);
        return logisticsInfoService.save(logisticsInfo);
    }
}
