package com.vvic.jst.controller;


import com.alibaba.fastjson.JSON;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.SecretException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.response.TradesSoldGetResponse;
import com.taobao.api.security.SecurityClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author zhongjunkang
 * @date 2020/8/29/0029
 * <pre>
 */
@RestController
@Slf4j
@RequestMapping("/kd/")
public class TestKDController {


    private String url = "https://eco.taobao.com/router/rest";


    @RequestMapping("syncKdTaobaoOrder")
    @ResponseBody
    public BaseResponse syncKdTaobaoOrder(@RequestParam(value = "token",required = false) String token)throws Exception{
        log.info("同步58");
        String appkey = "12145422";
        String secret = "05ec210fbc427e0b073219597b257edf";
        if (StringUtils.isEmpty(token)){
            token = "7001010022374d0037bd966bd3a09696af015659e878bc33a85587269037185d5e29ffe2924477203";
        }

        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        TradesSoldGetRequest req = new TradesSoldGetRequest();
        req.setFields("tid,receiver_name,receiver_state,receiver_address,receiver_zip,receiver_mobile,receiver_phone,orders");

        TradesSoldGetResponse response = client.execute(req , token);
        List<Trade> trades = response.getTrades();
        log.info("订单同步:{}", JSON.toJSONString(response));
        SecurityClient securityClient = new SecurityClient(new DefaultTaobaoClient(url, appkey, secret),"nWdWsN0IJGb8A2cccsH/9/fID60zKkYSdLBj/4xqbzA=");
//        String phone = securityClient.decrypt("$176$8nS+X3F9VKGfEjwq9ryDDA==$1$", "phone", token);
//        System.out.println("电话号码："+phone);
        String finalToken = token;
        trades.stream().forEach(trade -> {
            String add = null;
            try {
                add = securityClient.decrypt(trade.getReceiverName(), "simple", finalToken);
            } catch (SecretException e) {
                e.printStackTrace();
            }
            log.info("getReceiverName："+add);
        });

        trades.stream().forEach(trade -> {
            String add = null;
            try {
                add = securityClient.decrypt(trade.getReceiverAddress(), "simple", finalToken);
            } catch (SecretException e) {
                e.printStackTrace();
            }
            log.info("getReceiverAddress："+add);
        });

        trades.stream().forEach(trade -> {
            String add = null;
            try {
                add = securityClient.decrypt(trade.getReceiverMobile(), "simple", finalToken);
            } catch (SecretException e) {
                e.printStackTrace();
            }
            log.info("getReceiverMobile："+add);
        });
        System.out.println(response.getBody());
        log.info("--订单同步结果：{}", response);
        return new BaseResponse(response);
    }

    @RequestMapping("/testKDDecrypt")
    @ResponseBody
    public BaseResponse testKDDecrypt(@RequestParam(value = "token",required = false)String token) {
        String phone = null;
        try {
            String appkey = "1214542";
            String secret = "05ec210fbc427e0b073219597b257edf";
            if (StringUtils.isEmpty(token)){
                token = "7001010113090357489d39366c164653ed93836268161da4e157f97c6291232e9b7f4ce2924477203";
            }

            SecurityClient securityClient = new SecurityClient(new DefaultTaobaoClient(url, appkey, secret),"uQ1Mlk1PQ4FiyFONXlb6w3CGyKyVUCm6bjr+W6SZcCs=");
            phone = securityClient.decrypt("$176$3uAmQLWLyrW5Pf0ni2wWCw==$1$", "phone", token);
            System.out.println("电话号码："+phone);
        } catch (SecretException e) {
            e.printStackTrace();
            return new BaseResponse(500,e.getMessage());
        }
        return new BaseResponse(phone);
    }

}
