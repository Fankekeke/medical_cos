package cc.mrbird.febs.cos.controller;

import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.AlipayBean;
import cc.mrbird.febs.cos.service.PayService;
import com.alipay.api.AlipayApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cos/pay")
public class PayController {

    @Autowired
    private PayService payService;

    /**
     * 阿里支付
     *
     * @param outTradeNo  订单编号
     * @param subject     备注
     * @param totalAmount 付款金额
     * @param body        主体
     * @return 结果
     * @throws AlipayApiException 异常信息
     */
    @PostMapping(value = "/alipay")
    public R alipay(String outTradeNo, String subject, String totalAmount, String body) throws AlipayApiException {
        AlipayBean alipayBean = new AlipayBean();
        alipayBean.setOut_trade_no(outTradeNo);
        alipayBean.setSubject(subject);
        alipayBean.setTotal_amount(totalAmount);
        alipayBean.setBody(body);
        String result = payService.aliPay(alipayBean);
        return R.ok(result);
    }

}
