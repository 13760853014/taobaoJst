package com.vvic.jst.controller;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.SecretException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.response.TradesSoldGetResponse;
import com.taobao.api.security.SecurityClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
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
@RequestMapping("/test/")
public class TestController {



    @RequestMapping("/add")
    @ResponseBody
    public String add() {
        log.info("参数：{}");
        return "true";
    }

    private String url = "https://eco.taobao.com/router/rest";
    @RequestMapping("yncYjzsTaobaoOrder")
    @ResponseBody
    public BaseResponse yncYjzsTaobaoOrder(@RequestParam(value = "token",required = false) String token)throws Exception{
        log.info("同步易家助手");
        String appkey = "21108231";
        String secret = "1cda4f69156b91ca968e4c33383e2954";
        if (StringUtils.isEmpty(token)){
            token = "7001010022474d012770775a08045771cbc518c2e87fcdd5997b9caa88fb53f9814fcc72924477203";
        }

        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        TradesSoldGetRequest req = new TradesSoldGetRequest();
        req.setFields("tid,receiver_name,receiver_state,receiver_address,buyer_nick,receiver_city,receiver_district,receiver_zip,receiver_mobile,receiver_phone,service_orders");
        TradesSoldGetResponse response = client.execute(req , token);
        System.out.println(response.getBody());
        log.info("--订单同步结果：{}", response);
        BaseResponse baseResponse = new BaseResponse();

        String msg = "从塔外进入塔内。";
        SecurityClient securityClient = new SecurityClient(new DefaultTaobaoClient(url, appkey, secret),"uQ1Mlk1PQ4FiyFONXlb6w3CGyKyVUCm6bjr+W6SZcCs=");
        String phone = securityClient.decrypt("$176$8nS+X3F9VKGfEjwq9ryDDA==$1$", "phone", token);
        msg = msg+ "电话号码："+phone;
        baseResponse.setData(response);
         getDecrypt(token, appkey, secret, response.getTrades(), msg);
        baseResponse.setMessage(msg);
        baseResponse.setData(response.getTrades());
        return baseResponse;
    }

    private void getDecrypt( String token, String appkey, String secret, List<Trade> trades, String msg) {
        try {
            if (CollectionUtils.isEmpty(trades)){
                return;
            }
            SecurityClient securityClient = new SecurityClient(new DefaultTaobaoClient(url, appkey, secret),"uQ1Mlk1PQ4FiyFONXlb6w3CGyKyVUCm6bjr+W6SZcCs=");
            String phone = securityClient.decrypt("$176$8nS+X3F9VKGfEjwq9ryDDA==$1$", "phone", token);
            trades.stream().forEach(vo->{
                try {
                    boolean encryptData = SecurityClient.isEncryptData(vo.getReceiverMobile(), "phone");
                    if (encryptData){
                        String mobile = securityClient.decrypt(vo.getReceiverMobile(), "phone", token);
                        if(!StringUtils.isEmpty(mobile)){
                            vo.setReceiverMobile(mobile);
                        }
                    }
                    boolean encryptData2 = SecurityClient.isEncryptData(vo.getReceiverName(), "receiver_name");
                    if (encryptData){
                        String receiver_name = securityClient.decrypt(vo.getReceiverName(), "receiver_name", token);
                        if(!StringUtils.isEmpty(receiver_name)){
                            vo.setReceiverName(receiver_name);
                        }
                    }

                } catch (SecretException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("电话号码："+phone);
            msg = msg+ "电话号码："+phone;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("syncKdTaobaoOrder")
    @ResponseBody
    public BaseResponse syncKdTaobaoOrder(@RequestParam(value = "token",required = false) String token)throws Exception{
        log.info("同步58");
        String appkey = "1214542";
        String secret = "05ec210fbc427e0b073219597b257edf";
        if (StringUtils.isEmpty(token)){
            token = "7001010113090357489d39366c164653ed93836268161da4e157f97c6291232e9b7f4ce2924477203";
        }

        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        TradesSoldGetRequest req = new TradesSoldGetRequest();
        req.setFields("tid,receiver_name,receiver_state,receiver_address,receiver_zip,receiver_mobile,receiver_phone");
        TradesSoldGetResponse response = client.execute(req , token);
        List<Trade> trades = response.getTrades();
        SecurityClient securityClient = new SecurityClient(new DefaultTaobaoClient(url, appkey, secret),"nWdWsN0IJGb8A2cccsH/9/fID60zKkYSdLBj/4xqbzA=");
        String phone = securityClient.decrypt("$176$8nS+X3F9VKGfEjwq9ryDDA==$1$", "phone", token);
        System.out.println("电话号码："+phone);
        String finalToken = token;
        trades.stream().forEach(trade -> {
            String add = null;
            try {
                add = securityClient.decrypt(trade.getReceiverName(), "simple", finalToken);
            } catch (SecretException e) {
                e.printStackTrace();
            }
            log.info("地址："+add);
//            log.info("--订单信息：{}", securityClient.);
        });
        System.out.println(response.getBody());
        log.info("--订单同步结果：{}", response);
        return new BaseResponse(response);
    }
    @RequestMapping("/testYJZSDecrypt")
    @ResponseBody
    public BaseResponse testDecrypt(@RequestParam(value = "token",required = false)String token)throws Exception{
        try {
            String appkey = "21108231";
            String secret = "1cda4f69156b91ca968e4c33383e2954";
            if (StringUtils.isEmpty(token)){
                token = "7001010022474d012770775a08045771cbc518c2e87fcdd5997b9caa88fb53f9814fcc72924477203";
            }
            SecurityClient securityClient = new SecurityClient(new DefaultTaobaoClient(url, appkey, secret),"uQ1Mlk1PQ4FiyFONXlb6w3CGyKyVUCm6bjr+W6SZcCs=");
            String phone = securityClient.decrypt("$176$8nS+X3F9VKGfEjwq9ryDDA==$1$", "phone", token);
            System.out.println("电话号码："+phone);
            return new BaseResponse(phone);
        } catch (SecretException e) {
            e.printStackTrace();
            return new BaseResponse(500,e.getMessage());
        }
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
