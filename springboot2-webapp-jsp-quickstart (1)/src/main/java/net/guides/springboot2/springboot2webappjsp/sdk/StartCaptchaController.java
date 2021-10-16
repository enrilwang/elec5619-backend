package net.guides.springboot2.springboot2webappjsp.sdk;

import org.springframework.web.bind.annotation.RequestMapping;

import java.io.PrintWriter;
import java.util.HashMap;


import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
     * 获取滑块 验证
     * @author cjm
     *
     */
    @RestController
    public class StartCaptchaController {
        @RequestMapping(value = "startCaptcha", method = RequestMethod.GET)
        public void StartCaptcha(HttpServletRequest request,
                                 HttpServletResponse response) throws IOException{
            GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),true);
            String resStr = "{}";
// 自定义userid
            String userid = "test";
//自定义参数,可选择添加
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("user_id", userid); //网站用户id
            param.put("client_type", "web"); //web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
            param.put("ip_address", "127.0.0.1"); //传输用户请求验证时所携带的IP
            param.put("digestmod", "md5");
// 进行验证预处理
            int gtServerStatus = gtSdk.preProcess(param);
// 将服务器状态设置到session中
            request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
// 将userid设置到session中
            request.getSession().setAttribute("geetuserid", userid);


            resStr = gtSdk.getResponseStr();
            PrintWriter out = response.getWriter();
            out.println(resStr);
        }
    }

