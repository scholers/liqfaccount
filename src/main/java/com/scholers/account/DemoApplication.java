package com.scholers.account;

import com.scholers.account.util.ArithmeticUtils;
import com.scholers.account.util.HttpTool;
import net.sf.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;


@RestController
@SpringBootApplication
public class DemoApplication {

    private static String appkey = "94a418ccfee771f04a15879107d5bc45";

    private static String posturl = "http://op.juhe.cn/onebox/stock/query?key=94a418ccfee771f04a15879107d5bc45&dtype=";
    @RequestMapping("/")
    String index() {
        return "卫缺test spring boot！";
    }


    public static void parseJson(String jsonStr) {
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        double curpice = 0.0;
        double buynum = 2100.0;

            if(jsonObject.has("result")){
                JSONObject jsonObject2 = JSONObject.fromObject(jsonObject.getString("result"));
                // 首先把字符串转成 JSONArray  对象
                if(jsonObject2.has("currentPrice")) {
                    curpice = Double.parseDouble(jsonObject2.getString("currentPrice"));
                    double totalvalue = ArithmeticUtils.mul(buynum,curpice,2);
                    System.out.println("totalvalue ==" + totalvalue);
                }

            }

    }

    public static void main(String[] args) {

        String result = "";
        try {
            String a = URLEncoder.encode("腾讯", "UTF-8");//编码
            String para = "&stock=" + a + "\n";
            result =  HttpTool.sendPost("format=json", posturl + para, null);
        } catch (IOException ex){

        }
        System.out.println(result);
        parseJson(result);
        //SpringApplication.run(DemoApplication.class, args);
    }
}
