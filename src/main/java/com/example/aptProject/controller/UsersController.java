package com.example.aptProject.controller;

import com.example.aptProject.entity.MyRegion;
import com.example.aptProject.entity.Users;
import com.example.aptProject.service.LocationService;
import com.example.aptProject.service.MyRegionService;
import com.example.aptProject.service.UsersService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UsersController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private LocationService locationCodeService;
	@Autowired
	private UsersService uSvc;
	@Autowired
	private MyRegionService mSvc;
	@Autowired
	private LocationService lSvc;


	@GetMapping("/register")
	public String registerForm(Model model) {
		List<String> firstNames = locationCodeService.getAllFirstNames();
		model.addAttribute("firstNames", firstNames);
		return "user/register";
	}


	@GetMapping("/register/secondNames")
	@ResponseBody
	public List<String> getSecondNamesByFirstName(@RequestParam("firstName") String firstName) {
		List<String> secondNames = locationCodeService.getSecondNamesByFirstName(firstName);
		return secondNames;
	}


	@PostMapping("/register")
	public String registerProc(Model model,
							   String uid, String pwd, String pwd2, String uname, String email, String firstName, String secondName, String lastName) {

		if (uSvc.getUserByUid(uid) != null) {
			model.addAttribute("msg", "사용자 ID가 중복되었습니다.");
			model.addAttribute("url", "/apt/user/register");
			return "common/alertMsg";
		}
		if (pwd.equals(pwd2) && pwd != null) {
			Users user = new Users(uid, pwd, uname, email);
			uSvc.registerUser(user);

			if(lastName.equals("")){
				mSvc.registerUserBy2Names(user, firstName, secondName);
			}else{
				mSvc.registerUserBy3Names(user, firstName, secondName, lastName);
			}

			model.addAttribute("msg", "등록을 마쳤습니다. 로그인하세요.");
			model.addAttribute("url", "/apt/user/login");
			return "common/alertMsg";
		} else {
			model.addAttribute("msg", "패스워드 입력이 잘못되었습니다.");
			model.addAttribute("url", "/apt/user/register");
			return "common/alertMsg";
		}

	}

	@GetMapping("/login")
	public String loginForm() {
		return "user/login";
	}

	@PostMapping("/login")
	public String loginProc(String uid, String pwd, HttpSession session, Model model) {
		int result = uSvc.login(uid, pwd);
		switch (result) {
			case UsersService.CORRECT_LOGIN:
				Users user = uSvc.getUserByUid(uid);
				session.setAttribute("sessUid", uid);
				session.setAttribute("sessUname", user.getUname());
				session.setAttribute("email", user.getEmail());

				// 환영 메세지
				log.info("Info Login: {}, {}", uid, user.getUname());
				int LAWD_CD = 0;
				String DEAL_YMD = LocalDate.now().getYear() + String.format("%02d", LocalDate.now().getMonthValue() - 1);
				MyRegion myRegion = mSvc.getMyRegionByUid(uid);
				LAWD_CD = myRegion.getlCode();
				String url = "/apt/api/getResult/" + LAWD_CD + "/1";

				model.addAttribute("msg", user.getUname()+"님 환영합니다.");
				model.addAttribute("url", url);
//			model.addAttribute("url", "/apt/api/map");
				break;

			case UsersService.USER_NOT_EXIST:
				model.addAttribute("msg", "ID가 없습니다. 회원가입 페이지로 이동합니다.");
				model.addAttribute("url", "/apt/user/register");
				break;

			case UsersService.WRONG_PASSWORD:
				model.addAttribute("msg", "패스워드 입력이 잘못되었습니다. 다시 입력하세요.");
				model.addAttribute("url", "/apt/user/login");
		}
		return "common/alertMsg";
	}

	@GetMapping("/register/lastNames")
	@ResponseBody
	public List<String> getLastNamesByFirstNameAndSecondName(@RequestParam("firstName") String firstName,@RequestParam("secondName") String secondName) {
		List<String> lastNames = locationCodeService.getLastNamesByFirstNameAndSecondName(firstName, secondName);
		return lastNames;
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/user/login";
	}

	@GetMapping("/update")
	public String updateForm(HttpSession session, Model model) {
		String sessUid = (String) session.getAttribute("sessUid");
		if (sessUid != null) {
			List<String> firstNames = locationCodeService.getAllFirstNames();
			model.addAttribute("firstNames", firstNames);
			Users user = uSvc.getUserByUid(sessUid);
			model.addAttribute("user", user);
		} else {

			return "redirect:/user/login";
		}

		return "user/update";
	}
	@GetMapping("/update/secondNames")
	@ResponseBody
	public List<String> getSecondNamesByFirstNameForUpdate (@RequestParam("firstName") String firstName) {
		List<String> secondNames = locationCodeService.getSecondNamesByFirstName(firstName);
		return secondNames;
	}

	@GetMapping("/update/lastNames")
	@ResponseBody
	public List<String>getLastnamesByFristNamesANDSecondNamesForUpdate(@RequestParam("firstName") String firstName,@RequestParam("secondName") String secondName) {
		List<String> lastNames = locationCodeService.getLastNamesByFirstNameAndSecondName(firstName, secondName);
		return lastNames;
	}


	@PostMapping("/update")
	public String updateProc(Users user, String pwd, HttpSession session, Model model, String firstName, String secondName) {
		String sessUid = (String) session.getAttribute("sessUid");

		if (sessUid != null && sessUid.equals(user.getUid())) {
			uSvc.updateUser(user);
			mSvc.updateUser(user, firstName, secondName);

			model.addAttribute("msg", "회원 정보가 업데이트되었습니다.");
			model.addAttribute("url", "/apt/user/update");
		} else {

			model.addAttribute("msg", "잘못된 접근입니다.");
			model.addAttribute("url", "/apt/user/login");
		}

		return "redirect:/user/logout";

	}

	@GetMapping("/register/firstNames")
	public List<String> getAllFirstNames() {
		return locationCodeService.getAllFirstNames();
	}





}



