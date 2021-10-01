package net.guides.springboot2.springboot2webappjsp.controllers;
import org.springframework.util.DigestUtils;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class UserController {
	@Autowired
	UserRepository userRepo;

	@GetMapping("/users")
	public List<User> home() {
		return userRepo.findAll();


	}

	@RequestMapping("/login/oauth2")
	public String loginWithFB() {
		return "good";


	}


	@RequestMapping(value = "api/register", method = RequestMethod.POST)
	@CrossOrigin
	public Result register(@RequestBody User user) {
		Result result = new Result();

		if (user.getName() == null || user.getEmail() == null || user.getPassword() == null) {
			result.setMsg("Username and email_address and password cannot be null");
			return result;
		}

		User existUser = userRepo.getUserByEmail(user.getEmail());
		if (existUser != null) {
			result.setMsg("This user has already register");
			return result;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = sdf.format(new Date());
		User newUser = new User();
		newUser.setName(user.getName());
		newUser.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		newUser.setEmail(user.getEmail());
		newUser.setRegisterTime(format);


		User u1 = userRepo.save(newUser);
		if (u1 == null) {
			result.setMsg("register error");
			return result;
		}
		result.setMsg("register successful");
		result.setCode(201);
		return result;






	}

	//	login page
	@RequestMapping(value = "api/login", method = RequestMethod.POST)
	@CrossOrigin
	public Result login(@RequestBody User user, HttpSession session, HttpServletResponse response) throws UnsupportedEncodingException {
		Result result = new Result();

		if (user.getName() == null || user.getEmail() == null || user.getPassword() == null) {
			result.setMsg("Username and email_address and password cannot be null");
			return result;
		}

		User existUser = userRepo.getUserByEmail(user.getEmail());
		if (existUser == null) {
			result.setMsg("You have not registered, please sign in");
			result.setCode(400);
			return result;
		}

		if (!existUser.getPassword().equals(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))) {
			result.setMsg("Worng password, please try it again");
			result.setCode(400);
			return result;
		}


		Cookie cookie = new Cookie("email",user.getEmail());
		cookie.setMaxAge(24*60*60);
		Cookie cookie1 = new Cookie("userName", URLEncoder.encode(user.getName(),"UTF-8"));
		cookie1.setMaxAge(24*60*60);

		response.addCookie(cookie);
		response.addCookie(cookie1);

		result.setCode(200);
		result.setMsg("login successfully!");
		return result;
	}




	//User log out
	@RequestMapping(value = "api/logout")
	@CrossOrigin
	public Result logout(HttpServletResponse response) {
		Result result = new Result();
		Cookie cookie = new Cookie("email",null);
		cookie.setMaxAge(0);

		Cookie cookie1 = new Cookie("userName",null);
		cookie1.setMaxAge(0);

		response.addCookie(cookie);
		response.addCookie(cookie1);
		result.setMsg("logout successfully! ");
		result.setCode(200);
		return result;


	}




}
