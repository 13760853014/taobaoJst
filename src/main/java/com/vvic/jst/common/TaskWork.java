package com.vvic.jst.common;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.SecretException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.response.TradesSoldGetResponse;
import com.taobao.api.security.SecurityClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author zhongjunkang
 * @date 2020/11/16/0016
 * <pre>
 */
@Configuration
@Slf4j
public class TaskWork implements SchedulingConfigurer {

    private String url = "https://eco.taobao.com/router/rest";

    private String twUrl = "http://vvic.vaiwan.com/trade/open/taobao/authCallback";

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //设定一个长度20的定时任务线程池
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(20));
    }
    //@Scheduled(cron = "*/3 * * * * *")
    public void syncOrder() throws Exception {
        List<Trade> trades = syncYjzsOrder();
        ResponseEntity<String> entity = restTemplate.postForEntity(twUrl,trades , String.class);
        log.info("塔外返回参数:{},参数：{}", entity.toString(),trades);
    }

    private List<Trade> syncYjzsOrder() throws Exception{
        log.info("定时 同步易家助手");
        String appkey = "21108231";
        String secret = "1cda4f69156b91ca968e4c33383e2954";

        String  token = "7001010022474d012770775a08045771cbc518c2e87fcdd5997b9caa88fb53f9814fcc72924477203";

        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        TradesSoldGetRequest req = new TradesSoldGetRequest();
        req.setFields("tid,receiver_name,receiver_state,receiver_address,buyer_nick,receiver_city,receiver_district,receiver_zip,receiver_mobile,receiver_phone,service_orders");
        TradesSoldGetResponse response = client.execute(req , token);
        System.out.println(response.getBody());
        log.info("--订单同步结果：{}", response);

        String msg = "从塔外进入塔内。";
        SecurityClient securityClient = new SecurityClient(new DefaultTaobaoClient(url, appkey, secret),"uQ1Mlk1PQ4FiyFONXlb6w3CGyKyVUCm6bjr+W6SZcCs=");
        String phone = securityClient.decrypt("$176$8nS+X3F9VKGfEjwq9ryDDA==$1$", "phone", token);
        msg = msg+ "电话号码："+phone;
        getDecrypt(token, appkey, secret, response.getTrades(), msg);
        return response.getTrades();
    }

    private void getDecrypt(String token, String appkey, String secret, List<Trade> trades, String msg) {
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
                    if (encryptData2){
                        String receiver_name = securityClient.decrypt(vo.getReceiverName(), "receiver_name", token);
                        if(!StringUtils.isEmpty(receiver_name)){
                            vo.setReceiverName(receiver_name);
                        }
                    }
                    boolean buyerNickEncryptData = SecurityClient.isEncryptData(vo.getBuyerNick(), "nick");
                    if (buyerNickEncryptData){
                        String buyerNick = securityClient.decrypt(vo.getBuyerNick(), "nick", token);
                        if(!StringUtils.isEmpty(buyerNick)){
                            vo.setBuyerNick(buyerNick);
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


}
