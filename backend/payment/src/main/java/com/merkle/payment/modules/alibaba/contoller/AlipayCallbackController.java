package com.merkle.payment.modules.alibaba.contoller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merkle/payment/alipay/callback")
public class AlipayCallbackController extends AlipayBaseController {

    @GetMapping("/return/url")
    public void returnCallback() {
        //        TODO
    }

    @PostMapping("/notify/url")
    public void notifyCallback() throws Exception {

    }
}
