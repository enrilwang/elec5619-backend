package net.guides.springboot2.springboot2webappjsp.sdk;

import org.springframework.web.bind.annotation.RequestMapping;

import java.io.PrintWriter;
import java.util.HashMap;


import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


    @RestController
    public class StartCaptchaController {
        @RequestMapping(value = "startCaptcha", method = RequestMethod.GET)
        public void StartCaptcha(HttpServletRequest request,
                                 HttpServletResponse response) throws IOException{
            GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),true);
            String resStr = "{}";

            String userid = "test";

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("user_id", userid);
            param.put("client_type", "web");
            param.put("ip_address", "127.0.0.1");
            param.put("digestmod", "md5");

            int gtServerStatus = gtSdk.preProcess(param);

            request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);

            request.getSession().setAttribute("geetuserid", userid);


            resStr = gtSdk.getResponseStr();
            PrintWriter out = response.getWriter();
            out.println(resStr);
        }
    }

