package net.guides.springboot2.springboot2webappjsp.controllers;
import org.springframework.util.DigestUtils;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;

import java.nio.charset.StandardCharsets;
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




	@RequestMapping(value = "/register", method = RequestMethod.POST)
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
		result.setCode(200);
		return result;






	}







}
