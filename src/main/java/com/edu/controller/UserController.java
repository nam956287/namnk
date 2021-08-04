package com.edu.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.edu.model.User;
import com.edu.model.UserDAO;
import com.edu.services.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;


	@GetMapping("/")
	public String addOrEdit(ModelMap model) {
		User u = new User();
		model.addAttribute("USER", u);
		model.addAttribute("ACTION", "saveOrUpdate");
		return "register-user";
	}

	@PostMapping("/saveOrUpdate")
	public String saveOrUpdate(ModelMap model, @ModelAttribute("USER") User user) {
		userService.save(user);
		return "register-user";
	}

	@RequestMapping("list")
	public String list(ModelMap model, HttpSession session) {
		if (session.getAttribute("USERNAME") != null) {
			model.addAttribute("USERS", userService.findAll());
			return "view-user";
		}
		return "login";

	}

	@RequestMapping("/edit/{username}")
	public String edit(ModelMap model, @PathVariable(name = "username") String username) {

		Optional<User> u = userService.findById(username);
		if (u.isPresent()) {
			model.addAttribute("USER", u.get());
		} else {
			model.addAttribute("USER", new User());
		}
		model.addAttribute("USER", u);
		model.addAttribute("ACTION", "/saveOrUpdate");
		return "register-user";
	}

	@RequestMapping("/delete/{username}")
	public String delete(ModelMap model, @PathVariable(name = "username") String username) {

		userService.deleteById(username);
		model.addAttribute("USERS", userService.findAll());
		return "view-user";
	}

	// ======Login=====
	@RequestMapping("/login")
	public String showlogin() {
		return "login";
	}

	@PostMapping("/checklogin")
	public String checklogin(ModelMap model, @RequestParam("username") String username,
			@RequestParam("password") String password, HttpSession session) {

		if (userService.checkLogin(username, password)) {
			System.out.println("Login Success");
			session.setAttribute("USERNAME", username);
			model.addAttribute("USERS", userService.findAll());
			return "view-user";
		} else {
			System.out.println("Login Fail");
			model.addAttribute("ERROR", "Username or password not exist");
		}
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("USERNAME");
		return "redirect:/login";
	}
	@RequestMapping("/")
	public String showregister() {
		return "/";
	}
	
}
