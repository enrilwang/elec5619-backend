package net.guides.springboot2.springboot2webappjsp.configuration;

import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    UserRepository ur;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            return true;
//        }

        String token = request.getHeader("Authorization");

        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        if (token != null){
            String email = JwtUtil.getUserEmailByToken(request);

            String pass = ur.getUserByEmail(email).getPassword();
            boolean result = JwtUtil.verify(token,email,pass);
            if(result){
                System.out.println("access OK");
                return true;
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
