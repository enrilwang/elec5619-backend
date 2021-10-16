package net.guides.springboot2.springboot2webappjsp.sdk;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证测试 Controller
 * @author admin
 *
 */
@RestController
public class VerifyLoginController {
    @ResponseBody
    @RequestMapping("verifyLogin")
    public Map<String,String> verifyLogin(HttpServletRequest request,
                                          HttpServletResponse response) throws UnsupportedEncodingException {
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
                GeetestConfig.isnewfailback());
        String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
        String validate = request.getParameter(GeetestLib.fn_geetest_validate);
        String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);

        int gt_server_status_code = 1;

        String userid = (String)request.getSession().getAttribute("geetuserid");

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("user_id", userid);
        int gtResult = 0;
        if (gt_server_status_code == 1) {
//gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
            System.out.println(gtResult);
        } else {
// gt-server非正常情况下，进行failback模式验证
            System.out.println("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
            System.out.println(gtResult);
        }
        Map<String,String> data = new HashMap<>();
        if (gtResult == 1) {

            data.put("status", "success");
            data.put("version", gtSdk.getVersionInfo());
        } else {

            data.put("status", "fail");
            data.put("version", gtSdk.getVersionInfo());
        }
        return data;
    }
}
