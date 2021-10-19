package net.guides.springboot2.springboot2webappjsp.controllers;

import net.guides.springboot2.springboot2webappjsp.configuration.JwtUtil;
import net.guides.springboot2.springboot2webappjsp.domain.Artifact;
import net.guides.springboot2.springboot2webappjsp.domain.Subscribe;
import net.guides.springboot2.springboot2webappjsp.domain.SubscriptionType;
import net.guides.springboot2.springboot2webappjsp.repositories.ArtifactRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.SubscribeRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.SubscriptionTypeRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.util.DigestUtils;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//code 1 : failed
//code 0 : success

@RestController
public class UserController {
	@Autowired
	UserRepository userRepo;
	@Autowired
	ArtifactRepository artifactRepository;
	@Autowired
	SubscribeRepository sp;
	@Autowired
	SubscriptionTypeRepository subscriptionTypeRepository;


	//method for opening artifact page
	@RequestMapping(value = "getAllArtifactById", method = RequestMethod.GET)
	@CrossOrigin
	public Result getAllArtifactById(@RequestParam Integer id) {

		try {
			List<Map<String,Object>> artifacts = artifactRepository.findByUserId(id);
			if (artifacts.size() != 0) {
				Result result = new Result(0, "Query success!");
				result.setData(artifacts);
				return result;
			} else {
				return new Result(1, "No work exist!");
			}
		} catch (NullPointerException | EmptyResultDataAccessException | NoSuchElementException exception){
			return new Result(1, "Request fail!");
		}
	}

	@RequestMapping(value = "searchName", method = RequestMethod.POST)
	@CrossOrigin
	public Result searchName(@RequestParam String name) {
		Result result = new Result();
		List<User> users = userRepo.findByUserName(name);
		List<User> res = new ArrayList<>();
//		get creator user
		for (User s:users) {
			System.out.println("userName: " + s.getUsername());
			if (s.getIsCreator().equals("creator")) {
				res.add(s);
			}
		}
		List<List<Artifact>> res1 = new ArrayList<>();
		List<Artifact> all = artifactRepository.findAll();
		for (User s : res) {
			List<Artifact> art = new ArrayList<>() ;
			for (Artifact a : all) {
				if (a.getUser().getId().equals(s.getId())) {
					art.add(a);
				}
			}

			res1.add(art);
		}


		result.setCode(0);
		result.setMsg("OK");
		result.setData(res1);

		return result;
	}

//	check this user has subscribe or favourite of the creator
	@RequestMapping(value = "getUserById", method = RequestMethod.GET)
	@CrossOrigin
	public Result getUserById(HttpServletRequest request, @RequestParam Integer id) {
		Result result = new Result();
		String token = request.getHeader("Authorization");
		System.out.println("Token now is : " + token);
		if (token == null || token.equals("token is expired")) {

			User user;
			try {
				user = userRepo.getUserById(id);
			}catch (NullPointerException | EmptyResultDataAccessException | NoSuchElementException exception) {
				result.setMsg("get unsuccessful");
				result.setCode(1);
				return result;
			}


			List<Subscribe> currentsub =  sp.getSubscribeByCreatorId(id);


			SubscriptionType type = subscriptionTypeRepository.getSubscriptionTypeByUserId(id);


			HashMap<String,Object> res = new HashMap<>();
			res.put("user",user);
			res.put("subscribeType",type);
			res.put("subscribtionList", currentsub);

			result.setMsg("get successful");
			result.setCode(0);
			result.setData(res);
			return result;
		}else {
			String email = JwtUtil.getUserEmailByToken(request);
			User currentUser = userRepo.getUserByEmail(email);
			User user;
			try {
				user = userRepo.getUserById(id);
			}catch (NullPointerException | EmptyResultDataAccessException | NoSuchElementException exception) {
				result.setMsg("get unsuccessful");
				result.setCode(1);
				return result;
			}

			boolean favourite = false;
			boolean sub =false;
//		get favourite

			String subList = currentUser.getSubscribeId();
			String faList = currentUser.getFavoriteId();
			if (faList != null) {
				for(int i = 0;i < faList.length(); i++) {
					if (faList.charAt(i) ==  String.valueOf(user.getId()).charAt(0) ) {
						System.out.println("got it");
						favourite = true;
					}
				}
			}

//		get subscribe

			List<Subscribe> currentsub =  sp.getSubscribeByCreatorId(id);
			if (subList != null) {
				for(int i = 0;i < subList.length(); i++) {
					if (subList.charAt(i) ==  String.valueOf(user.getId()).charAt(0) ) {
						System.out.println("got it");
						sub = true;
					}
				}
			}



			SubscriptionType type = subscriptionTypeRepository.getSubscriptionTypeByUserId(id);


			List<Subscribe> currentsubList = sp.getSubscribeByUserIdAndCreatorIdAndActivated(currentUser.getId(),id,true);

			List<List<Map<String,Object>>> userScribe = new ArrayList<>();
			if (currentsubList.size() > 0) {
				for (Subscribe s : currentsubList) {
					List<Map<String,Object>> temp = artifactRepository.findByUserId(s.getCreatorId());
					userScribe.add(temp);
				}
			}


			HashMap<String,Object> res = new HashMap<>();
			res.put("user",user);
			res.put("subscribeType",type);
			res.put("subscribtionList", currentsub);
			res.put("favourite", favourite);
			res.put("userSubscirbtionList",currentsubList);
			res.put("userSubscirbtionListArtifact",userScribe);
			result.setMsg("get successful");
			result.setCode(0);
			result.setData(res);
			return result;
		}



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

		boolean flag =false;
		String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = null;
		matcher = regex.matcher(user.getEmail());
		flag = matcher.matches();
		if (!flag) {
			result.setMsg("email format does not correct");
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
		newUser.setProfilePicStore("/api/profilePicture/default.png");
		float b = 0.0f;
		newUser.setAccountBalance(b);

		userRepo.save(newUser);

		result.setMsg("register successful");
		result.setCode(0);
		return result;






	}

	//	login page
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@CrossOrigin
	public Result login( @RequestBody User user) throws UnsupportedEncodingException {
		Result result = new Result();
//		normal login

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

		HashMap<String,String> list = new HashMap<>();
		list.put("username",URLEncoder.encode(existUser.getUsername(),"UTF-8"));
		list.put("role",existUser.getIsCreator());

//		give token to client side
		String token = JwtUtil.sign(user.getEmail(),existUser.getPassword());
		if (token!= null) {
			result.setCode(0);
			result.setMsg("login successfully!");
			result.setData(list);
			result.setToken(token);
			return result;
		}

		result.setCode(1);
		result.setMsg("token failed!");

		return result;
	}




	//User log out
	@RequestMapping(value = "userlogout",  method = RequestMethod.POST)
	@CrossOrigin
	public Result logout() {
		Result result = new Result();

		result.setMsg("logout successfully! ");
		result.setCode(0);
		return result;


	}




}
