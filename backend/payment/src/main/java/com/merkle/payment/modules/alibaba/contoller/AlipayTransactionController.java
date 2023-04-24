package com.merkle.payment.modules.alibaba.contoller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.merkle.payment.annotation.MerkelResponseBody;
import com.merkle.payment.config.AlipayConfig;

@Controller
@RequestMapping("/merkle/payment/alipay/transaction")
public class AlipayTransactionController extends AlipayBaseController {
    @Autowired
    private AlipayConfig alipayConfig;

    @GetMapping("/test")
    @MerkelResponseBody
    public void test(HttpServletResponse httpResponse) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getUrl(),
                alipayConfig.getAppid(), alipayConfig.getPrivateKey(), alipayConfig.getFormat(),
                alipayConfig.getCharset(), alipayConfig.getPublicKey(), alipayConfig.getSigntype());
        AlipayTradePagePayRequest alipayRequest =  new  AlipayTradePagePayRequest(); //创建API对应的request
        alipayRequest.setReturnUrl( "http:127.0.0.1/merkle/payment/alipay/callback/return/url" );
        alipayRequest.setNotifyUrl( "http:127.0.0.1/merkle/payment/alipay/callback/notify/url" ); //在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent( "{"  +
                "\"out_trade_no\":\"20200320010101001\","  +
                "\"product_code\":\"FAST_INSTANT_TRADE_PAY\","  +
                "\"total_amount\":0.1,"  +
                "\"subject\":\"Iphone6 16G\","  +
                "\"body\":\"Iphone6 16G\","  +
                "\"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\","  +
                "\"extend_params\":{"  +
                "\"sys_service_provider_id\":\"2088621955089500\""  +
                "}" +
                "}" ); //填充业务参数
        AlipayTradePagePayResponse response = alipayClient.pageExecute(alipayRequest, "GET");
        if(response.isSuccess()){
            System.out.println("调用成功");
            String form= "" ;
            form = response.getBody();//调用SDK生成表单
            System.out.println(form);
            httpResponse.setContentType( "text/html;charset="  + alipayConfig.getCharset());
            httpResponse.getWriter().write(form); //直接将完整的表单html输出到页面
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
        } else {
            System.out.println("调用失败");
        }
    }
}
