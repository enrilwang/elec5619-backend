package net.guides.springboot2.springboot2webappjsp.controllers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


//code 1 : failed
//code 0 : success

@RestController
public class UserController {
	@Autowired
	UserRepository userRepo;


	@RequestMapping("/login/oauth2")
	public String loginWithFB() {
		return "good";


	}


	@RequestMapping(value = "register", method = RequestMethod.POST)
	@CrossOrigin
	public Result register(@RequestBody User user) {
		Result result = new Result();

		if (user.getEmail()== null || user.getPassword() == null) {
			result.setMsg("email_address and password cannot be null");
			result.setCode(1);
			return result;
		}

		User existUser = userRepo.getUserByEmail(user.getEmail());
		if (existUser != null) {
			result.setMsg("This user has already register");
			result.setCode(1);
			return result;
		}



		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = sdf.format(new Date());
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		newUser.setEmail(user.getEmail());
		newUser.setRegisterTime(format);
		newUser.setIsCreator("user");
		newUser.setIsAdmin("user");

		User u1 = userRepo.save(newUser);
		if (u1 == null) {
			result.setMsg("register error");
			return result;
		}
		result.setMsg("register successful");
		result.setCode(0);
		return result;






	}

	//	login page
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@CrossOrigin
	public Result login(@RequestBody User user, HttpServletResponse response) throws UnsupportedEncodingException {
		Result result = new Result();

		if ( user.getEmail() == null || user.getPassword() == null) {
			result.setMsg("Username and email_address and password cannot be null");
			return result;
		}

		User existUser = userRepo.getUserByEmail(user.getEmail());
		if (existUser == null) {
			result.setMsg("You have not registered, please sign in");
			result.setCode(1);
			return result;
		}

		if (!existUser.getPassword().equals(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))) {
			result.setMsg("Wrong password, please try it again");
			result.setCode(1);
			return result;
		}

		Cookie cookie = new Cookie("token",DigestUtils.md5DigestAsHex(user.getEmail().getBytes()));
		cookie.setMaxAge(24*60*60);

		Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
		boolean firstHeader = true;
		for (String header: headers) {
			if (firstHeader) {
				response.addHeader(HttpHeaders.SET_COOKIE,String.format("%s;%s",header,"SameSite=None"));
				firstHeader = false;
				continue;
			}
			response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s;%s",header,"SameSite=None"));
		}
		response.addCookie(cookie);

		HashMap<String,String> list = new HashMap<>();
		list.put("username",URLEncoder.encode(existUser.getUsername(),"UTF-8"));
		list.put("role",existUser.getIsCreator());

		result.setCode(0);
		result.setMsg("login successfully!");
		result.setData(list);
		return result;
	}




	//User log out
	@RequestMapping(value = "api/logout")
	@CrossOrigin
	public Result logout(HttpServletResponse response) {
		Result result = new Result();
		Cookie cookie = new Cookie("token",null);
		cookie.setMaxAge(0);



		response.addCookie(cookie);

		result.setMsg("logout successfully! ");
		result.setCode(0);
		return result;


	}




}
